#include "StdAfx.h"
#include "RuleEngine.h"
#include "Util.h"

RuleEngine::~RuleEngine()
{
	if(m_pRuleRoot != NULL) 
	{ 
		delete m_pRuleRoot; 
	}
}

bool RuleEngine::CompileRule(VARIANT xmlSource)
{
	IXMLDOMDocument2Ptr pDoc;
	IXMLDOMNodePtr pTmpNode;
	HRESULT hr;

	hr = pDoc.CreateInstance(__uuidof(DOMDocument40));

	if(FAILED(hr))
	{
		return false;
	}

	hr = m_pOutputDoc.CreateInstance(__uuidof(DOMDocument40));

	if(FAILED(hr))
	{
		return false;
	}

	pDoc->async =  VARIANT_FALSE;

	//::MessageBox(NULL, (char *) ((_bstr_t) xmlSource), "status", MB_OK);
		
	hr = pDoc->load(xmlSource);

	if(hr == VARIANT_TRUE)
	{
		//::MessageBox(NULL, "load successful!", "status", MB_OK);
		pTmpNode = pDoc->childNodes->Getitem(1);
		m_pRuleRoot = ProcessNode(pTmpNode);
	} 
	else
	{
		//::MessageBox(NULL, "load failed", "status", MB_OK);
		return false;
	}

	return true;
}

_bstr_t RuleEngine::Process()
{
	m_pRuleRoot->Eval();
	_bstr_t str(m_pOutputDoc->xml);
	return str;
}

MPNode* RuleEngine::ProcessNode(IXMLDOMNodePtr pNode)
{
	MPNode *pMPNode, *pMPNode2;
	NodeValue *pVal;
	IXMLDOMNodeListPtr pTmpNodes;
	IXMLDOMNodePtr pTmpNode;
	long length;
	_bstr_t name(pNode->nodeName);

	pMPNode = CreateMPNode(name);
	pVal = ::GetNodeValue(pNode);

	if(pMPNode == NULL)
	{
		return NULL;
	}

	if(pVal != NULL)
	{
		pMPNode->SetValue(pVal);
	}

	if(m_pDoc != NULL)
	{
		pMPNode->SetParser(m_pDoc);
	}

	if((!::IsBstrEmpty(pMPNode->GetNamespace()))
		&& (::IsBstrEmpty(m_Namespace) || m_Namespace != pMPNode->GetNamespace()))
	{
		m_Namespace = pMPNode->GetNamespace();
	}
	else if(!::IsBstrEmpty(m_Namespace) && ::IsBstrEmpty(pMPNode->GetNamespace()))
	{
		pMPNode->SetNamespace(m_Namespace);
	}

	if(m_pOutputDoc != NULL)
	{
		pMPNode->SetOutputDoc(m_pOutputDoc);
	}

	if(pNode->hasChildNodes() == VARIANT_TRUE)
	{
		pTmpNodes = pNode->childNodes;
		length = pTmpNodes->length;
		pTmpNode = NULL;
	
		for(long i = 0; i < length; i++)
		{
			pTmpNode = pTmpNodes->Getitem(i);
			pMPNode2 = ProcessNode(pTmpNode);
			if(pMPNode2 == NULL)
			{
				break;
			}
			pMPNode->AddChild(pMPNode2);
		}
	}

	return pMPNode;
}

MPNode* RuleEngine::CreateMPNode(_bstr_t &name)
{
	nodeType type;

	type = ::GetType(name);

	switch(type)
	{
	case VARIABLE:
		return new VariableNode();
	case CONDITION:
		return new ConditionNode();
	case OPERATOR:
		return new OperatorNode();
	case OR:
		return new OrNode();
	case AND:
		return new AndNode();
	case ACTION:
		return new ActionNode();
	case RULE:
		return new RuleNode();
	case RULESET:
		return new RuleSetNode();
	}

	return NULL;
}
