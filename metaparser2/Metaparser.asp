<%
  Dim mp
  Set mp = Server.CreateObject("ComMetaParserService20.MetaParser")
  mp.Init()
  mp.Parse ("" & Request.QueryString("doc"))
  mp.Process()
  Response.ContentType="text/xml"
  Response.write ("" & mp.Output())
%>