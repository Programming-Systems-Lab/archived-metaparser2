#include "StdAfx.h"
#include "Util.h"

bool IsEquals(_bstr_t arg1, const char *arg2)
{
	_bstr_t s(arg2);
	return arg1 == s;
}

bool IsBstrEmpty(_bstr_t &arg)
{
	static _bstr_t empty;
	return arg == empty;
}