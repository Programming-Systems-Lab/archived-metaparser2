// MetaParser.cpp : Implementation of CMetaParser
#include "stdafx.h"
#include "ComMetaParserService20.h"
#include "MetaParser.h"

/////////////////////////////////////////////////////////////////////////////
// CMetaParser

STDMETHODIMP CMetaParser::InterfaceSupportsErrorInfo(REFIID riid)
{
	static const IID* arr[] = 
	{
		&IID_IMetaParser
	};
	for (int i=0; i < sizeof(arr) / sizeof(arr[0]); i++)
	{
		if (InlineIsEqualGUID(*arr[i],riid))
			return S_OK;
	}
	return S_FALSE;
}

STDMETHODIMP CMetaParser::Init()
{
	bool status;

	CoInitialize(NULL);

	HRESULT hr;

	hr = m_pDoc.CreateInstance(__uuidof(DOMDocument40));

	if(FAILED(hr))
	{
		Error(_T("Error loading DOM parser!"));
		return E_FAIL;
	}

	status = LoadConfig();

	if(status == false)
	{
		Error(_T("Error reading configuration file!"));
		return E_FAIL;
	}

	m_RuleEngine.SetParser(m_pDoc);
	m_Output = "";

	return S_OK;
}

STDMETHODIMP CMetaParser::Parse(VARIANT xmlSource, VARIANT_BOOL *isSuccessful)
{
	IXMLDOMSchemaCollectionPtr pSchema;
	BSVector namespaces;
	long length;

	if(m_pDoc == NULL)
	{
		Error(_T("DOM Parser uninitialized!"));
		return E_FAIL;
	}

	m_pDoc->async = VARIANT_FALSE;

	if(m_Config.m_fResolveExternals == false)
	{
		m_pDoc->resolveExternals = VARIANT_FALSE;
	}
	else
	{
		m_pDoc->resolveExternals = VARIANT_TRUE;
	}

	if(m_Config.m_fValidate == false)
	{
		m_pDoc->validateOnParse = VARIANT_FALSE;
	}
	else
	{
		m_pDoc->validateOnParse = VARIANT_TRUE;
	}

	*isSuccessful = m_pDoc->load(xmlSource);

	if(*isSuccessful == VARIANT_TRUE)
	{
		pSchema = m_pDoc->namespaces;
		length = pSchema->length;
	
		for(long iIndex = 0; iIndex < length; iIndex++)
		{
			_bstr_t ns(pSchema->GetnamespaceURI(iIndex));
			namespaces.push_back(ns);
		}
		
		GetRuleURIs(namespaces);
	}
	else
	{
		Error(_T("Error parsing xml file!"));
		return E_FAIL;
	}

	return S_OK;
}

STDMETHODIMP CMetaParser::Process(VARIANT_BOOL *isSuccessful)
{
	bool status;

	*isSuccessful = VARIANT_FALSE;
	
	for(int i = 0; i < m_ruleURIs.size(); i++)
	{
		status = m_RuleEngine.CompileRule(_variant_t(m_ruleURIs[i]));

		if(status)
		{
			m_Output += m_RuleEngine.Process();
		}
	}

	*isSuccessful = VARIANT_TRUE;

	return S_OK;
}

STDMETHODIMP CMetaParser::Output(BSTR *xmlOutput)
{
	m_Output = "<?xml version=\"1.0\"?><Output>" + m_Output + "</Output>";
	*xmlOutput = m_Output.copy();

	return S_OK;
}

bool CMetaParser::LoadConfig()
{
	CComBSTR file;
	VARIANT_BOOL status;
	IXMLDOMNodeListPtr pNodes;
	IXMLDOMNodePtr pNode;
	long length;
	NodeValue *pVal;

	file.LoadString(IDS_CONFIGFILE);

	m_pDoc->async = VARIANT_FALSE;
	m_pDoc->resolveExternals = VARIANT_FALSE;
	m_pDoc->validateOnParse = VARIANT_FALSE;

	status = m_pDoc->load((BSTR) file);

	if(status == VARIANT_FALSE)
	{
		return false;
	}

	pNodes = m_pDoc->childNodes;

	if(pNodes == NULL)
	{
		return false;
	}

	pNode = pNodes->Getitem(1);

	if(pNode == NULL)
	{
		return false;
	}

	pNodes = pNode->childNodes;

	if(pNodes == NULL)
	{
		return false;
	}

	length = pNodes->length;

	for(long i = 0; i < length; i++)
	{
		pNode = pNodes->Getitem(i);
		
		pVal = ::GetNodeValue(pNode);

		if(pVal == NULL)
		{
			continue;
		}

		if(::IsEquals(pVal->m_Name, "validate"))
		{
			m_Config.m_fValidate = ((BoolValue *) pVal)->m_Value;			
		}
		else if(IsEquals(pVal->m_Name, "resolveExternals"))
		{
			m_Config.m_fResolveExternals = ((BoolValue *) pVal)->m_Value;
		}
		else if(IsEquals(pVal->m_Name, "baseURI"))
		{
			m_Config.m_BaseURI = ((StrValue *) pVal)->m_Value;
		}
	}

	return true;
}

bool CMetaParser::GetRuleURIs(BSVector &ns)
{
	for(int i = 0; i < ns.size(); i++)
	{
		_bstr_t uri = m_Config.m_BaseURI + ns[i];
		m_ruleURIs.push_back(uri);
	}
	return true;
}

