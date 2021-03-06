/* Generated By:JavaCC: Do not edit this line. MyParser.java */
package psl.tagprocessor.parser;

import java.util.Hashtable;
import siena.AttributeValue;
import org.apache.regexp.RE;

        public class MyParser implements MyParserConstants {
            Hashtable table;

            public void setHashtable(Hashtable ht) {
                table = ht;
            }

            AttributeValue getVariableValue(String key) {
                if(table.containsKey(key)) {
                    return (AttributeValue) table.get(key);
                }
                return null;
            }

            void initialize(String type, String name) {
                if(type.equals("boolean")) {
                    table.put(name, new AttributeValue(false));
                } else if(type.equals("int")) {
                    table.put(name, new AttributeValue((int) 0));
                } else if(type.equals("double")) {
                    table.put(name, new AttributeValue((double) 0.0D));
                } else if(type.equals("long")) {
                    table.put(name, new AttributeValue((long) 0L));
                } else if(type.equals("String")) {
                    table.put(name, new AttributeValue(""));
                }
            }

            void put(String key, AttributeValue value) {
                int type = ((AttributeValue) table.get(key)).getType();
                //System.out.println(key + " " + type);
                AttributeValue v = null;
                if(type == AttributeValue.BOOL) {
                    v = new AttributeValue(value.booleanValue());
                } else if(type == AttributeValue.INT) {
                    v = new AttributeValue(value.intValue());
                } else if(type == AttributeValue.LONG) {
                    v = new AttributeValue(value.longValue());
                } else if(type == AttributeValue.STRING) {
                    v = new AttributeValue(value.stringValue());
                } else if(type == AttributeValue.DOUBLE) {
                    v = new AttributeValue(value.doubleValue());
                }
                table.put(key, v);
            }

  final public AttributeValue parseOneLine() throws ParseException {
    AttributeValue value;
    switch (jj_nt.kind) {
    case INT:
    case LONG:
    case DOUBLE:
    case STRING:
    case BOOLEAN:
      VarDeclaration();
      jj_consume_token(SEPARATOR);
          //System.out.println("variable declaration"); 
          {if (true) return null;}
      break;
    case MINUS:
    case VARIABLE:
    case TRUE:
    case FALSE:
    case INTEGER_LITERAL:
    case FLOATING_POINT_LITERAL:
    case STRING_LITERAL:
    case NCName:
    case 41:
      value = Expr();
      jj_consume_token(SEPARATOR);
          //System.out.println("Expression is evaluated to: " + value); 
          {if (true) return value;}
      break;
    case SEPARATOR:
      jj_consume_token(SEPARATOR);
                      {if (true) return null;}
      break;
    default:
      jj_la1[0] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public void VarDeclaration() throws ParseException {
    String s;
    Token t;
    s = Type();
    t = jj_consume_token(NCName);
          initialize(s, t.image);
          //System.out.println("table content: " + table);
          //return null;	

  }

  final public String Type() throws ParseException {
    switch (jj_nt.kind) {
    case BOOLEAN:
      jj_consume_token(BOOLEAN);
                    {if (true) return new String("boolean");}
      break;
    case INT:
      jj_consume_token(INT);
                {if (true) return new String("int");}
      break;
    case DOUBLE:
      jj_consume_token(DOUBLE);
                   {if (true) return new String("double");}
      break;
    case LONG:
      jj_consume_token(LONG);
                 {if (true) return new String("long");}
      break;
    case STRING:
      jj_consume_token(STRING);
                   {if (true) return new String("String");}
      break;
    default:
      jj_la1[1] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public AttributeValue VariableReference() throws ParseException {
    Token t;
    jj_consume_token(VARIABLE);
    t = jj_consume_token(NCName);
          {if (true) return getVariableValue(t.image);}
    throw new Error("Missing return statement in function");
  }

  final public AttributeValue Expr() throws ParseException {
    AttributeValue value;
    if (jj_2_1(2147483647)) {
      value = Assignment();
                               {if (true) return value;}
    } else {
      switch (jj_nt.kind) {
      case MINUS:
      case VARIABLE:
      case TRUE:
      case FALSE:
      case INTEGER_LITERAL:
      case FLOATING_POINT_LITERAL:
      case STRING_LITERAL:
      case 41:
        value = ConditionalExpr();
                                    {if (true) return value;}
        break;
      default:
        jj_la1[2] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    throw new Error("Missing return statement in function");
  }

  final public AttributeValue Assignment() throws ParseException {
    AttributeValue value;
    Token t;
    t = jj_consume_token(NCName);
    jj_consume_token(ASSIGN);
    value = Expr();
          //System.out.println("assignment is called");
          put(t.image, value);
          //System.out.println("table content: " + table);
          {if (true) return new AttributeValue(true);}
    throw new Error("Missing return statement in function");
  }

  final public AttributeValue ConditionalExpr() throws ParseException {
    AttributeValue value;
    value = OrExpr();
                           {if (true) return value;}
    throw new Error("Missing return statement in function");
  }

  final public AttributeValue PrimaryExpr() throws ParseException {
    AttributeValue value;
    switch (jj_nt.kind) {
    case VARIABLE:
      value = VariableReference();
                                      {if (true) return value;}
      break;
    case 41:
      jj_consume_token(41);
      value = Expr();
      jj_consume_token(42);
                                    {if (true) return value;}
      break;
    case TRUE:
    case FALSE:
    case INTEGER_LITERAL:
    case FLOATING_POINT_LITERAL:
    case STRING_LITERAL:
      value = Literal();
                               {if (true) return value;}
      break;
    default:
      jj_la1[3] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public AttributeValue Literal() throws ParseException {
    Token t;
    AttributeValue value;
    switch (jj_nt.kind) {
    case INTEGER_LITERAL:
      t = jj_consume_token(INTEGER_LITERAL);
                                {if (true) return new AttributeValue(Integer.parseInt(t.image));}
      break;
    case FLOATING_POINT_LITERAL:
      t = jj_consume_token(FLOATING_POINT_LITERAL);
                                       {if (true) return new AttributeValue(Double.parseDouble(t.image));}
      break;
    case STRING_LITERAL:
      t = jj_consume_token(STRING_LITERAL);
          StringBuffer sb = new StringBuffer();
          String s = t.image;
          for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) != '"') {
              sb.append(s.charAt(i));
            }
          }
          {if (true) return new AttributeValue(sb.toString());}
      break;
    case TRUE:
    case FALSE:
      value = BooleanLiteral();
                                   {if (true) return value;}
      break;
    default:
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public AttributeValue BooleanLiteral() throws ParseException {
    switch (jj_nt.kind) {
    case TRUE:
      jj_consume_token(TRUE);
                 {if (true) return new AttributeValue(true);}
      break;
    case FALSE:
      jj_consume_token(FALSE);
                  {if (true) return new AttributeValue(false);}
      break;
    default:
      jj_la1[5] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

/*--------------*/
/* 3.4 Booleans */
/*--------------*/

/* [21] OrExpr    ::=    AndExpr | OrExpr 'or' AndExpr */
  final public AttributeValue OrExpr() throws ParseException {
    AttributeValue d1;
    AttributeValue d2;
    d1 = AndExpr();
    label_1:
    while (true) {
      switch (jj_nt.kind) {
      case OR:
        ;
        break;
      default:
        jj_la1[6] = jj_gen;
        break label_1;
      }
      jj_consume_token(OR);
      d2 = AndExpr();
            if(d1.booleanValue() || d2.booleanValue())
              d1 = new AttributeValue(true);
            else
              d1 = new AttributeValue(false);
    }
          {if (true) return d1;}
    throw new Error("Missing return statement in function");
  }

/* [22] AndExpr    ::=    EqualityExpr  | AndExpr 'and' EqualityExpr  */
  final public AttributeValue AndExpr() throws ParseException {
    AttributeValue d1;
    AttributeValue d2;
    d1 = EqualityExpr();
    label_2:
    while (true) {
      switch (jj_nt.kind) {
      case AND:
        ;
        break;
      default:
        jj_la1[7] = jj_gen;
        break label_2;
      }
      jj_consume_token(AND);
      d2 = EqualityExpr();
            if(d1.booleanValue() && d2.booleanValue())
              d1 = new AttributeValue(true);
            else
              d1 = new AttributeValue(false);
    }
          {if (true) return d1;}
    throw new Error("Missing return statement in function");
  }

/* [23] EqualityExpr    ::=    RelationalExpr | EqualityExpr '=' RelationalExpr | EqualityExpr '!=' RelationalExpr */
  final public AttributeValue EqualityExpr() throws ParseException {
    AttributeValue d1;
    AttributeValue d2;
    d1 = RelationalExpr();
    label_3:
    while (true) {
      switch (jj_nt.kind) {
      case MATCH:
      case EQ:
      case NEQ:
        ;
        break;
      default:
        jj_la1[8] = jj_gen;
        break label_3;
      }
      switch (jj_nt.kind) {
      case EQ:
        jj_consume_token(EQ);
        d2 = RelationalExpr();
            //System.out.println(d1 + "; " + d2);
            /* if(d1.getType() != d2.getType()) 
		return new AttributeValue(false); */
            if(d1.isEqualTo(d2))
                d1 = new AttributeValue(true);
            else
                d1 = new AttributeValue(false);
            //System.out.println(d1);

        break;
      case NEQ:
        jj_consume_token(NEQ);
        d2 = RelationalExpr();
            if(!d1.isEqualTo(d2))
                d1 = new AttributeValue(true);
            else
                d1 = new AttributeValue(false);
        break;
      case MATCH:
        jj_consume_token(MATCH);
        d2 = RelationalExpr();
              //System.out.println("d1: " + d1.stringValue());
              //System.out.println("d2: " + d2.stringValue());
            try {
              RE re = new RE(d2.stringValue());
              if(re.match(d1.stringValue()))
                  d1 = new AttributeValue(true);
              else
                  d1 = new AttributeValue(false);
            } catch(Exception e) {
              e.printStackTrace();
            }
        break;
      default:
        jj_la1[9] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
          {if (true) return d1;}
    throw new Error("Missing return statement in function");
  }

/* [24] RelationalExpr    ::=    AdditiveExpr | RelationalExpr '<' AdditiveExpr | RelationalExpr '>' AdditiveExpr  
					   | RelationalExpr '<=' AdditiveExpr  | RelationalExpr '>=' AdditiveExpr */
  final public AttributeValue RelationalExpr() throws ParseException {
    AttributeValue d1;
    AttributeValue d2;
    d1 = AdditiveExpr();
    label_4:
    while (true) {
      switch (jj_nt.kind) {
      case LT:
      case LTE:
      case GT:
      case GTE:
        ;
        break;
      default:
        jj_la1[10] = jj_gen;
        break label_4;
      }
      switch (jj_nt.kind) {
      case LT:
        jj_consume_token(LT);
        d2 = AdditiveExpr();
            if(d1.doubleValue() < d2.doubleValue())
              d1 = new AttributeValue(true);
            else
              d1 = new AttributeValue(false);
        break;
      case GT:
        jj_consume_token(GT);
        d2 = AdditiveExpr();
            if(d1.doubleValue() > d2.doubleValue())
              d1 = new AttributeValue(true);
            else
              d1 = new AttributeValue(false);
        break;
      case LTE:
        jj_consume_token(LTE);
        d2 = AdditiveExpr();
            if(d1.doubleValue() <= d2.doubleValue())
              d1 = new AttributeValue(true);
            else
              d1 = new AttributeValue(false);
        break;
      case GTE:
        jj_consume_token(GTE);
        d2 = AdditiveExpr();
            if(d1.doubleValue() >= d2.doubleValue())
              d1 = new AttributeValue(true);
            else
              d1 = new AttributeValue(false);
        break;
      default:
        jj_la1[11] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
          {if (true) return d1;}
    throw new Error("Missing return statement in function");
  }

/*-------------*/
/* 3.5 Numbers */
/*-------------*/

/* [25] AdditiveExpr ::= MultiplicativeExpr  | AdditiveExpr '+' MultiplicativeExpr  | AdditiveExpr '-' MultiplicativeExpr  */
  final public AttributeValue AdditiveExpr() throws ParseException {
    AttributeValue d1;
    AttributeValue d2;
    d1 = MultiplicativeExpr();
    label_5:
    while (true) {
      switch (jj_nt.kind) {
      case PLUS:
      case MINUS:
        ;
        break;
      default:
        jj_la1[12] = jj_gen;
        break label_5;
      }
      switch (jj_nt.kind) {
      case PLUS:
        jj_consume_token(PLUS);
        d2 = MultiplicativeExpr();
            d1 = new AttributeValue(d1.doubleValue()+d2.doubleValue());
        break;
      case MINUS:
        jj_consume_token(MINUS);
        d2 = MultiplicativeExpr();
            d1 = new AttributeValue(d1.doubleValue()-d2.doubleValue());
        break;
      default:
        jj_la1[13] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
          {if (true) return d1;}
    throw new Error("Missing return statement in function");
  }

/* [26] MultiplicativeExpr ::= UnaryExpr | MultiplicativeExpr MultiplyOperator UnaryExpr
			| MultiplicativeExpr 'div' UnaryExpr | MultiplicativeExpr 'mod' UnaryExpr  */
  final public AttributeValue MultiplicativeExpr() throws ParseException {
    AttributeValue d1;
    AttributeValue d2;
    d1 = UnaryExpr();
    label_6:
    while (true) {
      switch (jj_nt.kind) {
      case MOD:
      case DIV:
      case 43:
        ;
        break;
      default:
        jj_la1[14] = jj_gen;
        break label_6;
      }
      switch (jj_nt.kind) {
      case 43:
        jj_consume_token(43);
        d2 = UnaryExpr();
                                 d1 = new AttributeValue(d1.doubleValue()*d2.doubleValue());
        break;
      case DIV:
        jj_consume_token(DIV);
        d2 = UnaryExpr();
                                   d1 = new AttributeValue(d1.doubleValue()/d2.doubleValue());
        break;
      case MOD:
        jj_consume_token(MOD);
        d2 = UnaryExpr();
                                   d1 = new AttributeValue(d1.doubleValue()%d2.doubleValue());
        break;
      default:
        jj_la1[15] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
          {if (true) return d1;}
    throw new Error("Missing return statement in function");
  }

/* [27]    UnaryExpr    ::=    UnionExpr  | '-' UnaryExpr  */
  final public AttributeValue UnaryExpr() throws ParseException {
    AttributeValue d;
    switch (jj_nt.kind) {
    case VARIABLE:
    case TRUE:
    case FALSE:
    case INTEGER_LITERAL:
    case FLOATING_POINT_LITERAL:
    case STRING_LITERAL:
    case 41:
      d = PrimaryExpr();
                            {if (true) return d;}
      break;
    case MINUS:
      jj_consume_token(MINUS);
      d = UnaryExpr();
            {if (true) return new AttributeValue((-1) * d.doubleValue());}
      break;
    default:
      jj_la1[16] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_1();
    jj_save(0, xla);
    return retval;
  }

  final private boolean jj_3_1() {
    if (jj_scan_token(NCName)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(ASSIGN)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  public MyParserTokenManager token_source;
  ASCII_UCodeESC_CharStream jj_input_stream;
  public Token token, jj_nt;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  public boolean lookingAhead = false;
  private boolean jj_semLA;
  private int jj_gen;
  final private int[] jj_la1 = new int[17];
  final private int[] jj_la1_0 = {0x5ff80400,0x7c00000,0x50380400,0x50380000,0x50300000,0x300000,0x0,0x0,0x7000,0x7000,0x78000,0x78000,0x600,0x600,0x0,0x0,0x50380400,};
  final private int[] jj_la1_1 = {0x302,0x0,0x202,0x202,0x2,0x0,0x10,0x20,0x0,0x0,0x0,0x0,0x0,0x0,0x8c0,0x8c0,0x202,};
  final private JJCalls[] jj_2_rtns = new JJCalls[1];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  public MyParser(java.io.InputStream stream) {
    jj_input_stream = new ASCII_UCodeESC_CharStream(stream, 1, 1);
    token_source = new MyParserTokenManager(jj_input_stream);
    token = new Token();
    token.next = jj_nt = token_source.getNextToken();
    jj_gen = 0;
    for (int i = 0; i < 17; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public void ReInit(java.io.InputStream stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    token.next = jj_nt = token_source.getNextToken();
    jj_gen = 0;
    for (int i = 0; i < 17; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public MyParser(java.io.Reader stream) {
    jj_input_stream = new ASCII_UCodeESC_CharStream(stream, 1, 1);
    token_source = new MyParserTokenManager(jj_input_stream);
    token = new Token();
    token.next = jj_nt = token_source.getNextToken();
    jj_gen = 0;
    for (int i = 0; i < 17; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    token.next = jj_nt = token_source.getNextToken();
    jj_gen = 0;
    for (int i = 0; i < 17; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public MyParser(MyParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    token.next = jj_nt = token_source.getNextToken();
    jj_gen = 0;
    for (int i = 0; i < 17; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public void ReInit(MyParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    token.next = jj_nt = token_source.getNextToken();
    jj_gen = 0;
    for (int i = 0; i < 17; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken = token;
    if ((token = jj_nt).next != null) jj_nt = jj_nt.next;
    else jj_nt = jj_nt.next = token_source.getNextToken();
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    jj_nt = token;
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  final private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    return (jj_scanpos.kind != kind);
  }

  final public Token getNextToken() {
    if ((token = jj_nt).next != null) jj_nt = jj_nt.next;
    else jj_nt = jj_nt.next = token_source.getNextToken();
    jj_gen++;
    return token;
  }

  final public Token getToken(int index) {
    Token t = lookingAhead ? jj_scanpos : token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private java.util.Vector jj_expentries = new java.util.Vector();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      boolean exists = false;
      for (java.util.Enumeration enum = jj_expentries.elements(); enum.hasMoreElements();) {
        int[] oldentry = (int[])(enum.nextElement());
        if (oldentry.length == jj_expentry.length) {
          exists = true;
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              exists = false;
              break;
            }
          }
          if (exists) break;
        }
      }
      if (!exists) jj_expentries.addElement(jj_expentry);
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  final public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[44];
    for (int i = 0; i < 44; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 17; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 44; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.elementAt(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  final public void enable_tracing() {
  }

  final public void disable_tracing() {
  }

  final private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 1; i++) {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
          }
        }
        p = p.next;
      } while (p != null);
    }
    jj_rescan = false;
  }

  final private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

        }
