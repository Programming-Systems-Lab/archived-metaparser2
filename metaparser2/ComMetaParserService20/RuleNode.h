#ifndef _MP_RULENODE_H
#define _MP_RULENODE_H

#include "NodeValue.h"
#include <vector>

enum nodeType 
{  
	VARIABLE, 
	CONDITION,
	OPERATOR,
	OR,
	AND,
	ACTION,
	RULE,
	RULESET, 
	NOSUCHTYPE
};

enum operatorType
{
	EQ,
	NE,
	GT,
	GTE,
	LT,
	LTE, 
	MATCH
};

nodeType GetType(_bstr_t &t);

class MPNode 
{
public:
	MPNode() { m_pVal = NULL; }
	virtual ~MPNode();
	virtual void AddChild(MPNode* child);	
	virtual void PrintNode(int indent);
	virtual void SetValue(NodeValue *pVal) { m_pVal = pVal; }
	virtual void SetNamespace(_bstr_t &ns) { m_Namespace = ns; }
	virtual void SetParser(IXMLDOMDocument2Ptr pDoc) { m_pDoc = pDoc; }
	virtual void SetOutputDoc(IXMLDOMDocument2Ptr pOutputDoc) { m_pOutputDoc = pOutputDoc; }
	virtual _bstr_t GetNamespace() { return m_Namespace; }
	virtual NodeValue* Eval() = 0;

	nodeType m_Type;

protected:
	NodeValue *m_pVal;
	_bstr_t m_Namespace;
	IXMLDOMDocument2Ptr m_pDoc;
	IXMLDOMDocument2Ptr m_pOutputDoc;
	typedef std::vector<MPNode*> MPNodeList;
	MPNodeList m_Children;
};
	
class VariableNode : public MPNode
{
public:
	VariableNode();
	virtual ~VariableNode();
	virtual void PrintNode(int indent);
	virtual void SetValue(NodeValue *pVal);
	virtual NodeValue* Eval();
};

class ConditionNode : public MPNode
{
public:
	ConditionNode();
	virtual ~ConditionNode() { }
	virtual void PrintNode(int indent);
	virtual NodeValue* Eval();
	virtual void SetValue(NodeValue *pVal);

protected:
	bool Lte(VariableNode *a, VariableNode *b);
	bool Lt(VariableNode *a, VariableNode *b);
	bool Gte(VariableNode *a, VariableNode *b);
	bool Gt(VariableNode *a, VariableNode *b);
	bool Ne(VariableNode *a, VariableNode *b);
	bool Eq(VariableNode *a, VariableNode *b);
	bool Match(VariableNode *a, VariableNode *b);
};

class OperatorNode : public MPNode
{
public:
	OperatorNode();
	virtual ~OperatorNode() { }
	virtual void PrintNode(int indent);
	virtual NodeValue* Eval();
	virtual void SetValue(NodeValue *pVal);
};

class OrNode : public MPNode
{
public:
	OrNode();
	virtual ~OrNode() { }
	virtual void PrintNode(int indent);
	virtual NodeValue* Eval();
	virtual void SetValue(NodeValue *pVal);
};

class AndNode : public MPNode
{
public:
	AndNode();
	virtual ~AndNode() { }
	virtual void PrintNode(int indent);
	virtual NodeValue* Eval();
	virtual void SetValue(NodeValue *pVal);
};

class ActionNode : public MPNode
{
public:
	ActionNode();
	virtual ~ActionNode() { }
	virtual void PrintNode(int indent);
	virtual void SetValue(NodeValue *pVal);
	virtual NodeValue* Eval();
};

class RuleNode : public MPNode
{
public:
	RuleNode();
	virtual ~RuleNode() { }
	virtual void PrintNode(int indent);
	virtual NodeValue* Eval();
	virtual void SetValue(NodeValue *pVal);
};

class RuleSetNode : public MPNode
{
public:
	RuleSetNode();
	virtual ~RuleSetNode() { }
	virtual void PrintNode(int indent);
	virtual void SetValue(NodeValue *pVal);
	virtual NodeValue* Eval();
};
#endif