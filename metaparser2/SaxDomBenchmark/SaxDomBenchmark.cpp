// SaxDomBenchmark.cpp : Defines the entry point for the console application.

#include "stdafx.h"

#define CHR(err) { hr = (err) ; if (hr != S_OK && hr != S_ABORT_WANTED){ AtlTrace("\nError %08lx\n\n", err); return hr;} }
#define SIZE 100

int DOMParse();
int SAXParse();

wchar_t *sDir = L"Q:\\Project\\xml\\test\\";
wchar_t URL[SIZE][100];

__int64 frequency;

int main()
{
	wchar_t tmp[10];
	for(int i = 0; i < SIZE; i++)
	{
		::swprintf(tmp, L"b%d.xml", i+1);
		::wcscpy(URL[i], sDir);
		::wcscat(URL[i], tmp);
		//wprintf(L"%s\n", URL[i]);
	}

	::QueryPerformanceFrequency((LARGE_INTEGER *) &frequency);

	CoInitialize(NULL); 

	SAXParse();
	DOMParse();

	getchar();
	return 0;
}

int DOMParse()
{
	IXMLDOMDocument2Ptr pDoc = NULL;

	HRESULT hr;
	CHR(pDoc.CreateInstance(__uuidof(DOMDocument40)));
	
	pDoc->setProperty("NewParser", VARIANT_TRUE);
    pDoc->async =  VARIANT_FALSE;
	pDoc->resolveExternals = VARIANT_FALSE;
	pDoc->validateOnParse = VARIANT_FALSE;

	//pDoc->validateOnParse = VARIANT_TRUE;
	printf("DOM parse starts\n");	

	__int64 start, stop, total;
	QueryPerformanceCounter((LARGE_INTEGER *) &start);
	for(int i = 0; i < SIZE; i++)
	{
		VARIANT_BOOL res = pDoc->load(URL[i]);
		/*
		if(res != VARIANT_TRUE)
		{
			printf("Parse failed\n");
		}
		*/
	}
	QueryPerformanceCounter((LARGE_INTEGER *) &stop);
	total = ((stop - start) * 1000000) / frequency;

	printf("time is %I64d\n", total);

	printf("DOM parse finished successfully\n");

	pDoc = NULL;
	return 0;
}

int SAXParse()
{
	ISAXXMLReaderPtr pReader = NULL;

	HRESULT hr;
	CHR(pReader.CreateInstance(__uuidof(SAXXMLReader)));
	printf("SAX parse starts\n");

	//pReader->putFeature(L"schema-validation", VARIANT_TRUE);
	__int64 start, stop, total;
	QueryPerformanceCounter((LARGE_INTEGER *) &start);
	for(int i = 0; i < SIZE; i++)
	{
		CHR(pReader->parseURL(URL[i]));	
		//printf("\nParse result code: %08x\n\n",hr);
	}
	QueryPerformanceCounter((LARGE_INTEGER *) &stop);
	total = ((stop - start) * 1000000) / frequency;

	printf("time is %I64d\n", total);

	printf("SAX parse finishes successfully\n");

	pReader = NULL;
	return 0;
}
