// MetaParser.h : Declaration of the CMetaParser

#ifndef __METAPARSER_H_
#define __METAPARSER_H_

#include "resource.h"       // main symbols
#include "Util.h"
#include "NodeValue.h"
#include "RuleEngine.h"

typedef struct _MPConfig
{
	bool m_fResolveExternals;
	bool m_fValidate;
	_bstr_t m_BaseURI;
} MPConfig;

/////////////////////////////////////////////////////////////////////////////
// CMetaParser
class ATL_NO_VTABLE CMetaParser : 
	public CComObjectRootEx<CComSingleThreadModel>,
	public CComCoClass<CMetaParser, &CLSID_MetaParser>,
	public ISupportErrorInfo,
	public IDispatchImpl<IMetaParser, &IID_IMetaParser, &LIBID_COMMETAPARSERSERVICE20Lib>
{
public:
	CMetaParser()
	{
	}

DECLARE_CLASSFACTORY_AUTO_THREAD()

DECLARE_REGISTRY_RESOURCEID(IDR_METAPARSER)

DECLARE_PROTECT_FINAL_CONSTRUCT()

BEGIN_COM_MAP(CMetaParser)
	COM_INTERFACE_ENTRY(IMetaParser)
	COM_INTERFACE_ENTRY(IDispatch)
	COM_INTERFACE_ENTRY(ISupportErrorInfo)
END_COM_MAP()

// ISupportsErrorInfo
	STDMETHOD(InterfaceSupportsErrorInfo)(REFIID riid);

// IMetaParser
public:
	STDMETHOD(Output)(/*[out, retval]*/ BSTR *xmlOutput);
	STDMETHOD(Process)(/*[out, retval]*/ VARIANT_BOOL *isSuccessful);
	STDMETHOD(Parse)(/*[in]*/ VARIANT xmlSource, /*[out, retval]*/ VARIANT_BOOL *isSuccessful);
	STDMETHOD(Init)();
protected:
	typedef std::vector<_bstr_t> BSVector;

	bool GetRuleURIs(BSVector &ns);
	bool LoadConfig();

	MPConfig m_Config;
	BSVector m_ruleURIs;
	IXMLDOMDocument2Ptr m_pDoc;
	RuleEngine m_RuleEngine;
	_bstr_t m_Output;
};

#endif //__METAPARSER_H_
