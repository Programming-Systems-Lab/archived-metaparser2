<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" indent="no"/>

<xsl:template match="smartevent">
    <xsl:apply-templates/>
</xsl:template>


<xsl:template match="//tag">
  <rule id="1234"/>
</xsl:template>

<xsl:template match="//source/*|//time">
  <insert><xsl:value-of select="local-name(.)"/>=<xsl:value-of select="."/></insert>
</xsl:template>

<xsl:template match="temp">
  <xsl:choose>
    <xsl:when test="reading &lt; 32">
      <rule id="2000"/>
    </xsl:when>
    <xsl:when test="reading &gt; 90">
      <rule id="2001"/>
    </xsl:when>	
  </xsl:choose>
</xsl:template>

<xsl:template match="smtp">
  <xsl:if test="to='png3@cs.columbia.edu'">
    <rule id="psl"/>
    <insert>xpath=/smartevent/smtp/to</insert>
  </xsl:if>
  <xsl:if test="from='jjp32@cs.columbia.edu'">
    <rule id="psl"/>
  </xsl:if>
  <xsl:if test="subject='Urgent'">
    <rule id="1002"/>
  </xsl:if>
</xsl:template>

<xsl:template match="//SERVICE_DYNAMICS/*">
  <insert><xsl:value-of select="local-name(.)"/>=<xsl:value-of select="@value"/></insert>
</xsl:template>

</xsl:stylesheet>
