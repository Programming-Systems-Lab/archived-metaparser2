
ComMetaParserService20ps.dll: dlldata.obj ComMetaParserService20_p.obj ComMetaParserService20_i.obj
	link /dll /out:ComMetaParserService20ps.dll /def:ComMetaParserService20ps.def /entry:DllMain dlldata.obj ComMetaParserService20_p.obj ComMetaParserService20_i.obj \
		kernel32.lib rpcndr.lib rpcns4.lib rpcrt4.lib oleaut32.lib uuid.lib \

.c.obj:
	cl /c /Ox /DWIN32 /D_WIN32_WINNT=0x0400 /DREGISTER_PROXY_DLL \
		$<

clean:
	@del ComMetaParserService20ps.dll
	@del ComMetaParserService20ps.lib
	@del ComMetaParserService20ps.exp
	@del dlldata.obj
	@del ComMetaParserService20_p.obj
	@del ComMetaParserService20_i.obj
