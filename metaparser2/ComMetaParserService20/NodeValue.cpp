#include "StdAfx.h"
#include "NodeValue.h"
#include "Util.h"
#include <boost/regex.hpp>

void IntValue::SetValue(_bstr_t v)
{
	m_Value = ::atoi((char *) v);
}

void IntValue::Print()
{
	wprintf(L"%d\n", m_Value);
}

void RealValue::SetValue(_bstr_t v)
{
	m_Value = ::atof((char *) v);
}

void RealValue::Print()
{
	wprintf(L"%lf\n", m_Value);
}

void BoolValue::SetValue(_bstr_t v)
{
	if(IsEquals(v, "true"))
	{
		m_Value = true;
	}
	else
	{
		m_Value = false;
	}
}

void BoolValue::Print()
{
	if(m_Value == true)
	{
		wprintf(L"true\n");
	}
	else
	{
		wprintf(L"false\n");
	}
}

void StrValue::SetValue(_bstr_t v)
{
	m_Value = v;
}

void StrValue::Print()
{
	wprintf(L"%s\n", (wchar_t *) m_Value);
}

bool StrValue::Match(NodeValue *arg)
{
	if(arg->m_Type == n_STR)
	{
		boost::regex expression((char*) ((StrValue *) arg)->m_Value);

		if(boost::regex_match((char*) m_Value, expression))
		{
			return true;
		}
	}

	return false;
}

NodeValue* GetNodeValue(IXMLDOMNodePtr pNode)
{
	NodeValue *pVal;
	IXMLDOMNamedNodeMapPtr pAttributes;
	IXMLDOMNodePtr pAttrNode;
	_bstr_t n, v;

	pVal = NULL;
	pAttributes = pNode->attributes;
	
	n = "type";
	pAttrNode = pAttributes->getNamedItem(n);

	if(pAttrNode != NULL)
	{
		v = (_bstr_t) pAttrNode->GetnodeValue();
		if(IsEquals(v, "int"))
		{
			pVal = new IntValue();
		}
		else if(IsEquals(v, "real"))
		{
			pVal = new RealValue();
		}
		else if(IsEquals(v, "bool"))
		{
			pVal = new BoolValue();
		}
		else if(IsEquals(v, "string"))
		{
			pVal = new StrValue();
		}
	}

	n = "value";
	pAttrNode = pAttributes->getNamedItem(n);

	if(pAttrNode != NULL && pVal != NULL)
	{
		v = (_bstr_t) pAttrNode->GetnodeValue();
		pVal->SetValue(v);
	}

	n = "path";
	pAttrNode = pAttributes->getNamedItem(n);

	if(pAttrNode != NULL && pVal != NULL)
	{
		v = (_bstr_t) pAttrNode->GetnodeValue();
		pVal->m_Path = v;
	}	

	n = "namespace";
	pAttrNode = pAttributes->getNamedItem(n);

	if(pAttrNode != NULL)
	{
		v = (_bstr_t) pAttrNode->GetnodeValue();

		if(pVal == NULL)
		{
			pVal = new IntValue();
		}
	
		pVal->m_Namespace = v;
	}	

	n = "name";
	pAttrNode = pAttributes->getNamedItem(n);

	if(pAttrNode != NULL)
	{
		v = (_bstr_t) pAttrNode->GetnodeValue();

		if(pVal == NULL)
		{
			pVal = new IntValue();
		}

		pVal->m_Name = v;
	}	

	return pVal;
}

NodeValue* CopyNodeValue(NodeValue *pVal)
{
	NodeValue *pRetVal;

	pRetVal = 0;

	if(pVal->m_Type == n_INT)
	{
		pRetVal = new IntValue();
	}
	if(pVal->m_Type == n_REAL)
	{
		pRetVal = new RealValue();
	}
	if(pVal->m_Type == n_BOOL)
	{
		pRetVal = new BoolValue();
	}
	if(pVal->m_Type == n_STR)
	{
		pRetVal = new StrValue();
	}

	if(pRetVal == 0)
	{
		return NULL;
	}

	pRetVal->m_Name = pVal->m_Name;
	pRetVal->m_Namespace = pVal->m_Namespace;
	pRetVal->m_Path = pVal->m_Path;
	pRetVal->m_Type = pVal->m_Type;

	if(pRetVal->m_Type == n_INT)
	{
		((IntValue *) pRetVal)->m_Value = ((IntValue *) pVal)->m_Value;
	}
	if(pRetVal->m_Type == n_REAL)
	{
		((RealValue *) pRetVal)->m_Value = ((RealValue *) pVal)->m_Value;
	}
	if(pRetVal->m_Type == n_BOOL)
	{
		((BoolValue *) pRetVal)->m_Value = ((BoolValue *) pVal)->m_Value;
	}
	if(pRetVal->m_Type == n_STR)
	{
		((StrValue *) pRetVal)->m_Value = ((StrValue *) pVal)->m_Value;
	}

	return pRetVal;
}