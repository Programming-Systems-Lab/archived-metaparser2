#ifndef _MP_NODEVALUE_H
#define _MP_NODEVALUE_H

enum valueType
{
	n_INT,
	n_REAL,
	n_BOOL,
	n_STR
};

class NodeValue
{
public:
	NodeValue() { m_Type = 0; } 
	virtual ~NodeValue() { }
	
	virtual void SetValue(_bstr_t v) = 0;
	virtual void Print() = 0;

	virtual bool Lte(NodeValue *arg) = 0;
	virtual bool Lt(NodeValue *arg) = 0;
	virtual bool Gte(NodeValue *arg) = 0;
	virtual bool Gt(NodeValue *arg) = 0;
	virtual bool Ne(NodeValue *arg) = 0;
	virtual bool Eq(NodeValue *arg) = 0;
	virtual bool Match(NodeValue *arg) = 0;

	int m_Type;
	_bstr_t m_Name;
	_bstr_t m_Namespace;
	_bstr_t m_Path;
};

class IntValue : public NodeValue
{
public:
	IntValue() : NodeValue() { m_Type = n_INT; m_Value = 0; }
	virtual ~IntValue() { }

	virtual void SetValue(_bstr_t v);
	virtual void Print();
	
	virtual bool Lte(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value <= ((IntValue *) arg)->m_Value; }
	virtual bool Lt(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value < ((IntValue *) arg)->m_Value; }
	virtual bool Gte(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value >= ((IntValue *) arg)->m_Value; }
	virtual bool Gt(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value > ((IntValue *) arg)->m_Value; }
	virtual bool Ne(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value != ((IntValue *) arg)->m_Value; }
	virtual bool Eq(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value == ((IntValue *) arg)->m_Value; }
	virtual bool Match(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value == ((IntValue *) arg)->m_Value; }

	int m_Value;
};

class RealValue : public NodeValue
{
public:
	RealValue() : NodeValue() { m_Type = n_REAL; m_Value = 0.0; }
	virtual  ~RealValue() { }

	virtual void SetValue(_bstr_t v);
	virtual void Print();
	
	virtual bool Lte(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value <= ((RealValue *) arg)->m_Value; }
	virtual bool Lt(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value < ((RealValue *) arg)->m_Value; }
	virtual bool Gte(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value >= ((RealValue *) arg)->m_Value; }
	virtual bool Gt(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value > ((RealValue *) arg)->m_Value; }
	virtual bool Ne(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value != ((RealValue *) arg)->m_Value; }
	virtual bool Eq(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value == ((RealValue *) arg)->m_Value; }
	virtual bool Match(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value == ((RealValue *) arg)->m_Value; }

	double m_Value;
};

class BoolValue : public NodeValue
{
public:
	BoolValue() : NodeValue() { m_Type = n_BOOL; m_Value = false; }
	virtual ~BoolValue() { }

	virtual void SetValue(_bstr_t v);
	virtual void Print();
	
	virtual bool Lte(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value <= ((BoolValue *) arg)->m_Value; }
	virtual bool Lt(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value < ((BoolValue *) arg)->m_Value; }
	virtual bool Gte(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value >= ((BoolValue *) arg)->m_Value; }
	virtual bool Gt(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value > ((BoolValue *) arg)->m_Value; }
	virtual bool Ne(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value != ((BoolValue *) arg)->m_Value; }
	virtual bool Eq(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value == ((BoolValue *) arg)->m_Value; }
	virtual bool Match(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value == ((BoolValue *) arg)->m_Value; }

	bool m_Value;
};

class StrValue : public NodeValue
{
public:
	StrValue() : NodeValue() { m_Type = n_STR; m_Value = ""; }
	virtual ~StrValue() { }

	virtual void SetValue(_bstr_t v);
	virtual void Print();
	
	virtual bool Lte(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value <= ((StrValue *) arg)->m_Value; }
	virtual bool Lt(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value < ((StrValue *) arg)->m_Value; }
	virtual bool Gte(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value >= ((StrValue *) arg)->m_Value; }
	virtual bool Gt(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value > ((StrValue *) arg)->m_Value; }
	virtual bool Ne(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value != ((StrValue *) arg)->m_Value; }
	virtual bool Eq(NodeValue *arg) 
		{ return m_Type == arg->m_Type && m_Value == ((StrValue *) arg)->m_Value; }
	virtual bool Match(NodeValue *arg);

	_bstr_t m_Value;
};

NodeValue* GetNodeValue(IXMLDOMNodePtr pNode);
NodeValue* CopyNodeValue(NodeValue* pVal);

#endif