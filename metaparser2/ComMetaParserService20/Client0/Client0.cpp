// Client0.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"

int main(int argc, char* argv[])
{
	CoInitializeEx(0, COINIT_APARTMENTTHREADED);

	IMetaParserPtr pTest;
	HRESULT hr;
	_bstr_t output;

	hr = pTest.CreateInstance(__uuidof(MetaParser));
	
	if(FAILED(hr))
	{
		printf("CreateInstance failed\n");
	}
	else
	{
		try 
		{
			hr = pTest->Init();
			hr = pTest->Parse("http://www.columbia.edu/~sw375/Library.xml");
			hr = pTest->Process();
			output = pTest->Output();
			printf("The output is:\n%s\n", (char *) output);
		}
		catch(_com_error &e)
		{
			printf("%s\n", (char *) e.Description());
		}
	}

	getchar();

	pTest = NULL;

	CoUninitialize();
	return 0;
}

