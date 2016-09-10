package com.ztesoft.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


public class TestAn {
	
	
	private static double getCpuRateForLinux(){   
        InputStream is = null;   
        InputStreamReader isr = null;   
        BufferedReader brStat = null;   
        StringTokenizer tokenStat = null;   
        try{   
           //使用Linux的top指令获取系统cpu使用率
            Process process = Runtime.getRuntime().exec("top -b -n 1");   
            is = process.getInputStream();                     
            isr = new InputStreamReader(is);   
            brStat = new BufferedReader(isr);   
            
                brStat.readLine();   
                brStat.readLine();   
                     
                tokenStat = new StringTokenizer(brStat.readLine());   
                tokenStat.nextToken();   
                String usrUsage = tokenStat.nextToken();   
  
                       
                Float usage = new Float(usrUsage.substring(0,usrUsage.indexOf("%")));   
                 
                return (1-usage.floatValue()/100);   
            
              
        } catch(IOException ioe){   
	        try{   
	            if(is!=null)   
	                is.close();   
	            if(isr!=null)   
	                isr.close();   
	            if(brStat!=null)   
	            	brStat.close();   
	        }catch(IOException io){   
	            System.out.println(io.getMessage());   
	        }   
            return 1;   
        } finally{   
        	 try{   
 	            if(is!=null)   
 	                is.close();   
 	            if(isr!=null)   
 	                isr.close();   
 	            if(brStat!=null)   
 	            	brStat.close();   
 	        }catch(IOException e){   
 	            System.out.println(e.getMessage());   
 	        }    
        }   
  
    }   
	
	
	  
	  /**
	     * 功能：获取Linux系统cpu使用率
	     * */
	    public static float cpuUsage() {
	        Map<?, ?> map1 = TestAn.cpuinfo();
			Map<?, ?> map2 = TestAn.cpuinfo();
 
			long user1 = Long.parseLong(map1.get("user").toString());
			long nice1 = Long.parseLong(map1.get("nice").toString());
			long system1 = Long.parseLong(map1.get("system").toString());
			long idle1 = Long.parseLong(map1.get("idle").toString());
 
			long user2 = Long.parseLong(map2.get("user").toString());
			long nice2 = Long.parseLong(map2.get("nice").toString());
			long system2 = Long.parseLong(map2.get("system").toString());
			long idle2 = Long.parseLong(map2.get("idle").toString());
 
			long total1 = user1 + system1 + nice1;
			long total2 = user2 + system2 + nice2;
			float total = total2 - total1;
 
			long totalIdle1 = user1 + nice1 + system1 + idle1;
			long totalIdle2 = user2 + nice2 + system2 + idle2;
			float totalidle = totalIdle2 - totalIdle1;
 
			float cpusage = (total / totalidle) * 100;
			return  cpusage;
	       
	    }
	 
	    /**
	     * 功能：CPU使用信息
	     * */
	    public static Map<?, ?> cpuinfo() {
	        InputStreamReader inputs = null;
	        BufferedReader buffer = null;
	        Map<String, Object> map = new HashMap<String, Object>();
	        try {
	            inputs = new InputStreamReader(new FileInputStream("/proc/stat"));
	            buffer = new BufferedReader(inputs);
	            String line = "";
	            while (true) {
	                line = buffer.readLine();
	                if (line == null) {
	                    break;
	                }
	                if (line.startsWith("cpu")) {
	                    StringTokenizer tokenizer = new StringTokenizer(line);
	                    List<String> temp = new ArrayList<String>();
	                    while (tokenizer.hasMoreElements()) {
	                        String value = tokenizer.nextToken();
	                        temp.add(value);
	                    }
	                    map.put("user", temp.get(1));
	                    map.put("nice", temp.get(2));
	                    map.put("system", temp.get(3));
	                    map.put("idle", temp.get(4));
	                    map.put("iowait", temp.get(5));
	                    map.put("irq", temp.get(6));
	                    map.put("softirq", temp.get(7));
	                    map.put("stealstolen", temp.get(8));
	                    break;
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                buffer.close();
	                inputs.close();
	            } catch (Exception e2) {
	                e2.printStackTrace();
	            }
	        }
	        return map;
	    }
	  
	
	public static void main(String[] args) {
//		System.out.println(Configure.IOM_LOG_PATH);
//		
//		
//		System.out.println(new File(Configure.IOM_LOG_PATH).getTotalSpace()/ 1024 / 1024 );
//		System.out.println(new File(Configure.IOM_LOG_PATH).getFreeSpace()/ 1024 / 1024 );

		String msg = "{  \"ServiceOrder\": {    \"OrderInfo\": {      \"MainServerId\": \"33000\",      \"BrandCode\": \"3G00\",      \"NetCode\": \"33\",      \"SerialNo\": \"9815122323462481\",      \"NetType\": \"1\",      \"ReSendTag\": \"0\",      \"BusiType\": \"0\",      \"AcceptType\": \"0\",      \"LocalNetId\": \"71\",      \"StaffId\": \"wangshuang24\",      \"StaffName\": \"王爽\",      \"CoNbr\": \"9815122323462481\",      \"CityCode\": \"974146\",      \"DepartId\": \"9722559\",      \"DepartName\": \"七台河市分公司-行业应用运营支撑中心\",      \"AcceptDate\": \"2015-12-23 11:43:46\",      \"SoNbr\": \"9815122323462481\",      \"ProdId\": \"90396164\",      \"TradeTypeCode\": \"1028\",      \"UserId\": \"170912275412118\",      \"SoType\": \"0\"    },    \"CustInfos\": {      \"CustInfo\": {        \"ActType\": \"X\",        \"ContactName\": \"李春光\",        \"AddressInfo\": \"黑龙江省七台河市桃山区桃北街道十七委3组\",        \"ContactPhone\": \"15604641177\",        \"CustName\": \"李春光\",        \"CustLevelId\": \"0\",        \"CertName\": \"18位身份证\",        \"CertTypeId\": \"1\",        \"CertCode\": \"230903197909060324\",        \"CustId\": \"170912284913946\",        \"CustTypeName\": \"个人客户\",        \"CustTypeId\": \"0\",        \"CustLevelName\": \"普通\"      }    },    \"ServiceInfo\": {      \"ProdInfo\": {        \"MainProdInfo\": {          \"ActType\": \"X\",          \"BrandCode\": \"3G00\",          \"ProdId\": \"90396164\",          \"StartDate\": \"2009-12-27 21:27:50\",          \"UserId\": \"170912275412118\",          \"MainServerId\": \"33000\"        },        \"SoNbrInfos\": {          \"SoNbrInfo\": {            \"ActType\": \"X\",            \"AccNbr\": \"18604641838\"          }        },        \"SimCardInfos\": {          \"SimCardInfo\": {            \"ActType\": \"X\",            \"OpPtr\": \"1\",            \"Ki\": \"92E1FFD237F659A6C721D3EC3CF46731\",            \"IsRemote\": \"1\",            \"Imsi\": \"460014640664781\",            \"SwitchId\": \"GH10\",            \"SimNo\": \"8986011589701910012\",            \"CardType\": \"912\"          }        }      }    },    \"GroupInfos\": {      \"GroupInfo\": {        \"GroupId\": \"14643932333\",        \"GroupInstId\": \"2913082813908263\",        \"GroupType\": \"7T\",        \"GroupProdId\": \"8500\",        \"GroupTypeName\": \"省份VPDN集团成员关系\",        \"ActType\": \"M\",        \"MemberNum\": \"1\",        \"MemberInfos\": {          \"MemberInfo\": {            \"Seq\": \"0\",            \"UserId\": \"170912275412118\",            \"SoNbr\": \"9815122323462481\",            \"AccNbr\": \"18604641838\",            \"BrandCode\": \"3G00\",            \"MainServerId\": \"33000\",            \"ProdId\": \"90396164\",            \"MainFlag\": \"Y\",            \"ActType\": \"M\",            \"MemberPrptyInfos\": {              \"MemberPrptyInfo\": [                {                  \"ActType\": \"X\",                  \"PrptyId\": \"VPN_NAME\",                  \"PrptyName\": \"集团名称\",                  \"PrptyValue\": \"暂无\"                },                {                  \"ActType\": \"X\",                  \"PrptyId\": \"VPDN_JTIPLX\",                  \"PrptyName\": \"集团IP类型\",                  \"PrptyValueCode\": \"3\",                  \"PrptyValue\": \"自动IP\"                },                {                  \"ActType\": \"X\",                  \"PrptyId\": \"MEM_IFSHIELDWEB\",                  \"PrptyName\": \"是否屏蔽公网\",                  \"PrptyValueCode\": \"0\",                  \"PrptyValue\": \"屏蔽公网\"                },                {                  \"ActType\": \"X\",                  \"PrptyId\": \"MEM_CUST_NAME\",                  \"PrptyName\": \"成员客户名称\"                },                {                  \"ActType\": \"X\",                  \"PrptyId\": \"DOMAIN_VPDN\",                  \"PrptyName\": \"域名\",                  \"PrptyValue\": \"HLJLJ-BANK.WXATM.HLAPN\"                },                {                  \"ActType\": \"X\",                  \"PrptyId\": \"MEM_PRODUCT_NAME\",                  \"PrptyName\": \"成员产品\"                },                {                  \"ActType\": \"M\",                  \"PrptyId\": \"MEM_SETIP\",                  \"PrptyName\": \"IP地址\",                  \"PrptyValue\": \"10.12.184.200\"                },                {                  \"ActType\": \"X\",                  \"PrptyId\": \"MEM_SERIAL_NUMBER\",                  \"PrptyName\": \"成员业务号码\",                  \"PrptyValue\": \"18604641838\"                },                {                  \"ActType\": \"X\",                  \"PrptyId\": \"MEM_ROLE_CODE\",                  \"PrptyName\": \"成员角色\",                  \"PrptyValueCode\": \"0\",                  \"PrptyValue\": \"G网成员\"                }              ]            }          }        }      }    }  }}L_E_D";
		long p1 = System.currentTimeMillis();
		boolean res = msg.endsWith("L_E_D");
		long p2 = System.currentTimeMillis();
		System.out.println((p2-p1) +"   "+res);
		
//		if(args[0].equals("1")){
//			
//			if(System.getProperty("os.name").equals("Linux")){
//				System.out.println(new java.text.DecimalFormat("0.000").format(getCpuRateForLinux()));
//			}
//			
//		}else if((args[0].equals("2"))){
//			System.out.println(cpuUsage());
//		}
		
		
	}

}
