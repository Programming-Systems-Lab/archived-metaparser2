// stdafx.h : include file for standard system include files,

#if !defined(AFX_STDAFX_H__D9607847_D343_49FD_B95A_F822032E608D__INCLUDED_)
#define AFX_STDAFX_H__D9607847_D343_49FD_B95A_F822032E608D__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#define WIN32_LEAN_AND_MEAN		// Exclude rarely-used stuff from Windows headers

#include <stdio.h>

//For pure SAX interface and you don't want to trap com error as C++ exception, 
//you can do #import with raw_interfaces_only
#import <msxml4.dll> 

//otherwise, uncomment this line
//#import <msxml3.dll> high_method_prefix("_") raw_method_prefix("")
using namespace MSXML2;
#include <assert.h>
#include <atlbase.h>

void dump_com_error(_com_error &e);

void dump_com_error(_com_error &e)
{
	printf("Error\n");
	printf("\a\tCode = %08lx\n", e.Error());
	printf("\a\tCode meaning = %s", e.ErrorMessage());
	_bstr_t bstrSource(e.Source());
	_bstr_t bstrDescription(e.Description());
	printf("\a\tSource = %s\n", (LPCSTR) bstrSource);
	printf("\a\tDescription = %s\n", (LPCSTR) bstrDescription);
}

#define S_ABORT_WANTED ((HRESULT)0x800400caL)

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_STDAFX_H__D9607847_D343_49FD_B95A_F822032E608D__INCLUDED_)
