#include "StdAfx.h"
#include "RuleNode.h"
#include "Util.h"

#define SIZE 9

_bstr_t nodeName[] =
{
	"variable",
	"condition",
	"operator",
	"or",
	"and",
	"action",
	"rule",
	"ruleset"
};

_bstr_t valueType[] = 
{
	"int",
	"real",
	"bool",
	"string"
};

nodeType GetType(_bstr_t &t)
{
	for(int i = 0; i < SIZE; i++)
	{
		if(nodeName[i] == t)
			return (nodeType) i;
	}
	return NOSUCHTYPE;
}

MPNode::~MPNode()
{ 
	if(m_pVal != NULL)
	{
		delete m_pVal;
	}

	for(int i = 0; i < m_Children.size(); i++)
	{
		if(0 != m_Children[i])
		{
			delete m_Children[i];
		}
	}
}

void MPNode::AddChild(MPNode* child)
{
	m_Children.push_back(child);
}

void MPNode::PrintNode(int indent)
{
	MPNode *node;

	for(int i = 0; i < indent; i++)
		printf(" ");
	
	wprintf(L"%s\n", (wchar_t *) nodeName[m_Type]);

	for(i = 0; i < m_Children.size(); i++)
	{
		node = m_Children[i];
		node->PrintNode(indent + 2);
	}
}

VariableNode::VariableNode()
{
	m_Type = VARIABLE;
}

VariableNode::~VariableNode()
{

}

void VariableNode::PrintNode(int indent)
{
	printf("\n");
	wprintf(L"name: variable\n");
	wprintf(L"type: %d\n", m_pVal->m_Type);
	wprintf(L"path: %s\n", (wchar_t *) m_pVal->m_Path);
	wprintf(L"value: ");
	m_pVal->Print();
	printf("\n");
}

void VariableNode::SetValue(NodeValue *pVal)
{
	MPNode::SetValue(pVal);
	PrintNode(0);
}

NodeValue* VariableNode::Eval()
{
	IXMLDOMSelectionPtr pSelection;
	IXMLDOMNodePtr pNode;
	NodeValue* pVal;

	pVal = CopyNodeValue(m_pVal);

	if(!::IsBstrEmpty(m_pVal->m_Path))
	{
		m_pDoc->setProperty(L"SelectionNamespaces", m_Namespace);
		m_pDoc->setProperty(L"SelectionLanguage", L"XPath");

		IXMLDOMNodeListPtr pNodeList = m_pDoc->selectNodes(m_pVal->m_Path);
		printf("# of items selected: %ld\n", pNodeList->length);
		
		pSelection = pNodeList;
		//only select the first node
		pNode = pSelection->nextNode();

		if(pNode->nodeType == NODE_ELEMENT)
		{
			_bstr_t text(pNode->text);
			pVal->SetValue(text);
			wprintf(L"Using path: %s\n", (wchar_t *) text);
		}
		else if(pNode->nodeType == NODE_ATTRIBUTE)
		{
			_bstr_t text = (_bstr_t) pNode->GetnodeValue();
			pVal->SetValue(text);
			wprintf(L"Using path: %s\n", (wchar_t *) text);
		}
	}

	return pVal;
}

ConditionNode::ConditionNode()
{
	m_Type = CONDITION;
}

void ConditionNode::PrintNode(int indent)
{
	MPNode::PrintNode(indent);
}

NodeValue* ConditionNode::Eval()
{
	//printf("Eval\n");
	VariableNode *v1, *v2;
	OperatorNode *o;
	BoolValue *pVal;
	int opCode;

	if(m_Children.size() != 3)
	{
		return NULL;
	}

	pVal = new BoolValue();

	v1 = (VariableNode *) m_Children[0];
	o = (OperatorNode *) m_Children[1];
	v2 = (VariableNode *) m_Children[2];
	
	opCode = ((IntValue *) o->Eval())->m_Value;

	pVal->m_Value = false;

	switch(opCode)
	{
	case EQ:
		if(Eq(v1, v2))
		{
			pVal->m_Value = true;
		}
		break;
	case NE:
		if(Ne(v1, v2))
		{
			pVal->m_Value = true;
		}
		break;
	case GT:
		if(Gt(v1, v2))
		{
			pVal->m_Value = true;
		}
		break;
	case GTE:
		if(Gte(v1, v2))
		{
			pVal->m_Value = true;
		}
		break;
	case LT:
		if(Lt(v1, v2))
		{
			pVal->m_Value = true;
		}
		break;
	case LTE:
		if(Lte(v1, v2))
		{
			pVal->m_Value = true;
		}
		break;
	case MATCH:
		if(Match(v1, v2))
		{
			pVal->m_Value = true;
		}
		break;
	}
	
	if(pVal->m_Value == true)
	{
		//::MessageBox(NULL, "Condition evaluated to true", "Info", MB_OK);
	}

	return pVal;
}

void ConditionNode::SetValue(NodeValue *pVal)
{

}

bool ConditionNode::Eq(VariableNode *a, VariableNode *b)
{
	return a->Eval()->Eq(b->Eval());
}

bool ConditionNode::Ne(VariableNode *a, VariableNode *b)
{	
	return a->Eval()->Ne(b->Eval());
}

bool ConditionNode::Gt(VariableNode *a, VariableNode *b)
{
	return a->Eval()->Gt(b->Eval());
}

bool ConditionNode::Gte(VariableNode *a, VariableNode *b)
{
	return a->Eval()->Gte(b->Eval());
}

bool ConditionNode::Lt(VariableNode *a, VariableNode *b)
{
	return a->Eval()->Lt(b->Eval());
}

bool ConditionNode::Lte(VariableNode *a, VariableNode *b)
{
	return a->Eval()->Lte(b->Eval());
}

bool ConditionNode::Match(VariableNode *a, VariableNode *b)
{
	return a->Eval()->Match(b->Eval());
}

OperatorNode::OperatorNode()
{
	m_Type = OPERATOR;
}

void OperatorNode::PrintNode(int indent)
{
	MPNode::PrintNode(indent);
}

void OperatorNode::SetValue(NodeValue *pVal)
{
	_bstr_t v;

	m_pVal = new IntValue();

	if(m_pVal == NULL)
	{
		return;
	}

	v = ((StrValue *) pVal)->m_Value;

	if(::IsEquals(v, "EQ"))
	{
		((IntValue *) m_pVal)->m_Value = EQ;
	}
	else if(::IsEquals(v, "NE"))
	{
		((IntValue *) m_pVal)->m_Value = NE;
	}
	else if(::IsEquals(v, "GT"))
	{
		((IntValue *) m_pVal)->m_Value = GT;
	}
	else if(::IsEquals(v, "GTE"))
	{
		((IntValue *) m_pVal)->m_Value = GTE;
	}
	else if(::IsEquals(v, "LT"))
	{
		((IntValue *) m_pVal)->m_Value = LT;
	}
	else if(::IsEquals(v, "LTE"))
	{
		((IntValue *) m_pVal)->m_Value = LTE;
	}
	else if(::IsEquals(v, "MATCH"))
	{
		((IntValue *) m_pVal)->m_Value = MATCH;
	}
}

NodeValue* OperatorNode::Eval()
{
	NodeValue *pVal;
	pVal = CopyNodeValue(m_pVal);
	return pVal;
}

OrNode::OrNode()
{
	m_Type = OR;
}

void OrNode::PrintNode(int indent)
{
	MPNode::PrintNode(indent);
}

NodeValue* OrNode::Eval()
{
	BoolValue *pVal;
	pVal = new BoolValue();

	pVal->m_Value = false;

	for(int i = 0; i < m_Children.size(); i++)
	{
		if(((BoolValue *) m_Children[i]->Eval())->m_Value == true)
		{
			pVal->m_Value = true;
			break;
		}
	}

	return pVal;
}

void OrNode::SetValue(NodeValue *pVal)
{

}

AndNode::AndNode()
{
	m_Type = AND;
}

void AndNode::PrintNode(int indent)
{
	MPNode::PrintNode(indent);
}

NodeValue* AndNode::Eval()
{
	BoolValue *pVal;
	pVal = new BoolValue();

	pVal->m_Value = true;

	for(int i = 0; i < m_Children.size(); i++)
	{
		if(((BoolValue *) m_Children[i]->Eval())->m_Value == FALSE)
		{
			pVal->m_Value = false;
			break;
		}
	}
	return pVal;
}

void AndNode::SetValue(NodeValue *pVal)
{

}

RuleNode::RuleNode()
{
	m_Type = RULE;
}

void RuleNode::PrintNode(int indent)
{
	MPNode::PrintNode(indent);
}

NodeValue* RuleNode::Eval()
{
	BoolValue *pVal;
	pVal = new BoolValue();

	pVal->m_Value = false;

	if(m_Children.size() == 0)
	{
		return pVal;
	}

	if(((BoolValue *) m_Children[0]->Eval())->m_Value == true)
	{
		pVal->m_Value = true;

		//rules 
		for(int i = 1; i < m_Children.size(); i++)
		{
			m_Children[i]->Eval();
		}
	}
	return pVal;
}

void RuleNode::SetValue(NodeValue *pVal)
{

}

ActionNode::ActionNode()
{
	m_Type = ACTION;
}

void ActionNode::PrintNode(int indent)
{
	MPNode::PrintNode(indent);
}

NodeValue* ActionNode::Eval()
{
	wchar_t buf[256], tb1[128], tb2[128];
	char act[256];
	int charNeeded;

	IXMLDOMElementPtr pElement;

	::wcscpy(buf, m_pVal->m_Name);
	::wcscpy(tb1, L"=");

	if(m_pVal->m_Type == n_INT)
	{
		::swprintf(tb2, L"%d", ((IntValue *) m_pVal)->m_Value);
	}
	else if(m_pVal->m_Type == n_REAL)
	{
		::swprintf(tb2, L"%lf", ((RealValue *) m_pVal)->m_Value);
	}
	else if(m_pVal->m_Type == n_BOOL)
	{
		if(((BoolValue *) m_pVal)->m_Value == true)
		{
			::wcscpy(tb2, L"true");
		}
		else if(((BoolValue *) m_pVal)->m_Value == true)
		{
			::wcscpy(tb2, L"false");
		}
	}
	else if(m_pVal->m_Type == n_STR)
	{
		::wcscpy(tb2, ((StrValue *) m_pVal)->m_Value);
	}

	::wcscat(buf, tb1);
	::wcscat(buf, tb2);

	pElement = m_pOutputDoc->createElement("action");
	pElement->setAttribute("name", m_pVal->m_Name);
	pElement->setAttribute("type", valueType[m_pVal->m_Type]);
	pElement->setAttribute("value", tb2);

	//pElement->text = tb2;

	if(m_pOutputDoc->documentElement == NULL)
	{
		m_pOutputDoc->documentElement = m_pOutputDoc->createElement("Result");
		m_pOutputDoc->documentElement->setAttribute("namespace", m_Namespace);
	}

	m_pOutputDoc->documentElement->appendChild(pElement);

	charNeeded = ::WideCharToMultiByte(CP_ACP, 0, buf, wcslen(buf), 0, 0, NULL, NULL);

	::WideCharToMultiByte(CP_ACP, 0, buf, wcslen(buf), act, charNeeded, NULL, NULL);
	act[charNeeded] = '\0';

	//::MessageBox(NULL, act, "action", MB_OK);
	
	return NULL;
}

void ActionNode::SetValue(NodeValue *pVal)
{
	MPNode::SetValue(pVal);
}

RuleSetNode::RuleSetNode()
{
	m_Type = RULESET;
}

void RuleSetNode::PrintNode(int indent)
{
	MPNode::PrintNode(indent);
}

NodeValue* RuleSetNode::Eval()
{
	for(int i = 0; i < m_Children.size(); i++)
	{
		m_Children[i]->Eval();
	}

	return NULL;
}

void RuleSetNode::SetValue(NodeValue *pVal)
{
	MPNode::SetValue(pVal);
	
	if(!::IsBstrEmpty(m_pVal->m_Namespace))
	{
		m_Namespace = m_pVal->m_Namespace;
	}
}


