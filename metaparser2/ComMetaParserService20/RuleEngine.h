#ifndef _MP_RULEENGINE_H
#define _MP_RULEENGINE_H

#include "RuleNode.h"

class RuleEngine
{
public:
	RuleEngine() { m_pDoc = NULL; m_pOutputDoc = NULL; m_pRuleRoot = NULL; }
	~RuleEngine(); 

	bool CompileRule(VARIANT xmlSource);
	_bstr_t Process();
	void SetParser(IXMLDOMDocument2Ptr pDoc) { m_pDoc = pDoc; }

protected:
	MPNode* ProcessNode(IXMLDOMNodePtr pNode);
	MPNode* CreateMPNode(_bstr_t &name);

	_bstr_t m_Namespace;
	IXMLDOMDocument2Ptr m_pDoc;
	IXMLDOMDocument2Ptr m_pOutputDoc;
	MPNode *m_pRuleRoot;
};

#endif