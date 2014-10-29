package couch;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonObject;

public class SubtestDoc {
	private String 	name, subtestId, prototype;
	private Logger logger = Logger.getLogger(SubtestDoc.class.getName());
	private Sum 	sum;
	private Data data;
	
	public SubtestDoc() {
		sum = new Sum(0,0,0,0);
		
	}
	/**
	 * Get Sub test Name
	 * @return String
	 */
	public String getname(){
		return name;
	}
	/**
	 * Get Sub Test Id 
	 * @return String
	 */
	public String getsubtestId(){
		return subtestId;
	}
	/**
	 * Get Sub Test Prototype
	 * @return String
	 */
	public String getprototype(){
		return prototype;
	}
	/**
	 * Get Sub sum info
	 * @return Sum
	 */
	public Sum getsum(){
		return sum;
	}
	/**
	 * Setting Sub Name
	 * @param name
	 */
	public void setname(String name){
		data = new Data();
		this.name = name;
	}
	public void setloc(String name){
		data.addsubname(name);
	}
	/**
	 * Setting Sub ID
	 * @param subId
	 */
	public void setsubtestId(String subId){
		this.subtestId = subId;
	}
	/**
	 * Setting Sub Prototype
	 * @param proto
	 */
	public void setprototype(String proto){
		this.prototype = proto;
	}
	/**
	 * Setting Sub sum Field
	 * @param cor
	 * @param inco
	 * @param miss
	 * @param tot
	 */
	public void setsum(int cor, int inco,int miss,int tot){
		this.sum.setcorrect(cor);
		this.sum.setincorrect(inco);
		this.sum.setmissing(miss);
		this.sum.settotal(tot);
	}
	/**
	 * Converting into Object
	 * @return JsonObject
	 */
	public JsonObject toJson(){
		JsonObject json = new JsonObject();
		if(name != null)
		json.addProperty("name",name);
		if(subtestId != null)
		json.addProperty("subtestId",subtestId);
		if(prototype != null)
		json.addProperty("prototype",prototype);
		if(sum != null)
		json.add("sum",sum.toJson());
		if(data != null)
		json.add("data", data.toJson());
		return json;
	}
	public void setdata(String prototype){
		if(prototype.equals("complete")){
			data = new Data();
			String com = "Omr Generated Results";
			long endtime = System.currentTimeMillis();
			data.addcomp(com, endtime, null, null,null,null,null,null,null);
		}
	}
	/**
	 * 
	 * @param json all Documents by Download key
	 * @param reqproto 
	 * @param quesorder
	 * @param results
	 * @param opt
	 */
	/***
	 *	SETDATA:::::: HEADERS getSeq{"0":"question-117-1047795231","1":"question-118-979563237","2":"question-119-1005037867","3":"question-120-520036099","4":"question-667-1396945893","5":"question-668-1187281297","6":"question-669-1623912644","7":"question-121-796065227","8":"question-105-2142807538","9":"question-107-1744839881","10":"question-102-1880935784","11":"question-583-1624803855","12":"question-585-445094037","13":"question-587-74303096","14":"question-576-2117083535","15":"question-578-2049238864","16":"question-451-2007436719","17":"question-453-741561078","18":"question-455-997108174","19":"question-582-2112358728","20":"question-580-1935111821","21":"question-272-2138917627","22":"question-274-883740209","23":"question-349-412630097","24":"question-351-234761944","25":"question-353-1309942767","26":"question-513-1071157718","27":"question-515-70853121","28":"question-517-1287624486","29":"question-519-381843848","30":"question-327-575584215","31":"question-329-696016145","32":"question-331-31427730","33":"question-333-1144491277","34":"question-355-864330735","35":"question-357-83211364","36":"question-359-1368050911","37":"question-361-759103077","38":"question-595-1807415456","39":"question-597-2061907088","40":"question-599-1188292869","41":"question-601-1406120845","42":"question-603-1136730553","43":"question-605-283976918","44":"question-607-395626391","45":"question-609-1761875214","46":"question-611-613196319","47":"question-613-232922485","48":"question-615-1381523439","49":"question-617-453421796","50":"question-619-94353126","51":"question-621-814009194","52":"question-623-491963058","53":"question-625-1746821422","54":"question-295-150254409","55":"question-297-2077851884","56":"question-299-1696992469","57":"question-301-1753697130","58":"question-303-1307554427","59":"question-305-577218401","60":"question-307-545056034","61":"question-309-539813733","62":"question-86-872787356","63":"question-90-1839034754","64":"question-94-959964423","65":"question-98-1603685855","66":"question-87-405977676","67":"question-91-536005366","68":"question-95-1740932901","69":"question-99-1496731002","70":"question-388-535158684","71":"question-230-1846429499","72":"question-232-954931865","73":"question-234-920067257","74":"question-236-372325360","75":"question-183-1627814699","76":"question-185-1603473258","77":"question-187-899353468","78":"question-189-922450327","79":"question-493-1655080129","80":"question-495-136398333","81":"question-497-997832497","82":"question-499-1552757802","83":"question-501-626464877"}
		SETDATA:::::: HEADERS results[skip, B, skip, C, B, D, skip, skip, skip, skip, skip, skip, skip, skip, skip, B, skip, skip, skip, skip, skip, skip, skip, skip, skip, skip, skip, B, B, skip, skip, skip, A, C, C, skip, A, B, B, skip, skip, B, skip, skip, skip, B, skip, skip, skip, skip, skip, C, skip, skip, C, skip, C, A, skip, skip, C, skip, skip, skip, skip, skip, skip, skip, C, A, skip, A, B, skip, B, skip, A, skip, C, skip, A, skip, B, skip]
		SETDATA:::::: HEADERS seqdoc.getoptSeq(){"question-117-1047795231":{"A":"352","B":"353","C":"354"},"question-118-979563237":{"A":"355","B":"356","C":"357"},"question-119-1005037867":{"A":"358","B":"359","C":"360"},"question-120-520036099":{"A":"361","B":"362","C":"363"},"question-667-1396945893":{"A":"2052","B":"2053","C":"2054"},"question-668-1187281297":{"A":"2055","B":"2056","C":"2057","D":"2058"},"question-669-1623912644":{"A":"2059","B":"2060","C":"2061"},"question-121-796065227":{"A":"364","B":"365","C":"366"},"question-105-2142807538":{"A":"315","B":"316","C":"317"},"question-107-1744839881":{"A":"321","B":"322","C":"323"},"question-102-1880935784":{"A":"305","B":"306","C":"307","D":"308"},"question-583-1624803855":{"A":"1795","B":"1796","C":"1797","D":"1798"},"question-585-445094037":{"A":"1803","B":"1804","C":"1805"},"question-587-74303096":{"A":"1810","B":"1811","C":"1812"},"question-576-2117083535":{"A":"1771","B":"1772","C":"1773","D":"1774"},"question-578-2049238864":{"A":"1778","B":"1779","C":"1780"},"question-451-2007436719":{"A":"1393","B":"1394","C":"1395","D":"1396"},"question-453-741561078":{"A":"1400","B":"1401","C":"1402"},"question-455-997108174":{"A":"1406","B":"1407","C":"1408"},"question-582-2112358728":{"A":"1792","B":"1793","C":"1794"},"question-580-1935111821":{"A":"1785","B":"1786","C":"1787","D":"1788"},"question-272-2138917627":{"A":"825","B":"826","C":"827","D":"828"},"question-274-883740209":{"A":"832","B":"833","C":"834"},"question-349-412630097":{"A":"1073","B":"1074","C":"1075","D":"1076"},"question-351-234761944":{"A":"1081","B":"1082","C":"1083","D":"1084"},"question-353-1309942767":{"A":"1089","B":"1090","C":"1091","D":"1092"},"question-513-1071157718":{"A":"1580","B":"1581","C":"1582"},"question-515-70853121":{"A":"1586","B":"1587","C":"1588"},"question-517-1287624486":{"A":"1592","B":"1593","C":"1594"},"question-519-381843848":{"A":"1598","B":"1599","C":"1600"},"question-327-575584215":{"A":"992","B":"993","C":"994","D":"995"},"question-329-696016145":{"A":"1000","B":"1001","C":"1002","D":"1003"},"question-331-31427730":{"A":"1008","B":"1009","C":"1010","D":"1011"},"question-333-1144491277":{"A":"1016","B":"1017","C":"1018","D":"1019"},"question-355-864330735":{"A":"1097","B":"1098","C":"1099","D":"1100"},"question-357-83211364":{"A":"1105","B":"1106","C":"1107","D":"1108"},"question-359-1368050911":{"A":"1113","B":"1114","C":"1115"},"question-361-759103077":{"A":"1119","B":"1120","C":"1121"},"question-595-1807415456":{"A":"1836","B":"1837","C":"1838"},"question-597-2061907088":{"A":"1842","B":"1843","C":"1844"},"question-599-1188292869":{"A":"1848","B":"1849","C":"1850"},"question-601-1406120845":{"A":"1854","B":"1855","C":"1856"},"question-603-1136730553":{"A":"1860","B":"1861","C":"1862"},"question-605-283976918":{"A":"1866","B":"1867","C":"1868"},"question-607-395626391":{"A":"1872","B":"1873","C":"1874"},"question-609-1761875214":{"A":"1878","B":"1879","C":"1880"},"question-611-613196319":{"A":"1884","B":"1885","C":"1886"},"question-613-232922485":{"A":"1890","B":"1891","C":"1892"},"question-615-1381523439":{"A":"1896","B":"1897","C":"1898"},"question-617-453421796":{"A":"1902","B":"1903","C":"1904"},"question-619-94353126":{"A":"1908","B":"1909","C":"1910"},"question-621-814009194":{"A":"1914","B":"1915","C":"1916"},"question-623-491963058":{"A":"1920","B":"1921","C":"1922"},"question-625-1746821422":{"A":"1926","B":"1927","C":"1928"},"question-295-150254409":{"A":"895","B":"896","C":"897"},"question-297-2077851884":{"A":"901","B":"902","C":"903"},"question-299-1696992469":{"A":"907","B":"908","C":"909"},"question-301-1753697130":{"A":"913","B":"914","C":"915"},"question-303-1307554427":{"A":"919","B":"920","C":"921"},"question-305-577218401":{"A":"925","B":"926","C":"927"},"question-307-545056034":{"A":"931","B":"932","C":"933"},"question-309-539813733":{"A":"937","B":"938","C":"939"},"question-86-872787356":{"A":"256","B":"257","C":"258"},"question-90-1839034754":{"A":"268","B":"269","C":"270"},"question-94-959964423":{"A":"280","B":"281","C":"282"},"question-98-1603685855":{"A":"292","B":"293","C":"294"},"question-87-405977676":{"A":"259","B":"260","C":"261"},"question-91-536005366":{"A":"271","B":"272","C":"273"},"question-95-1740932901":{"A":"283","B":"284","C":"285"},"question-99-1496731002":{"A":"295","B":"296","C":"297"},"question-388-535158684":{"A":"1205","B":"1206"},"question-230-1846429499":{"A":"698","B":"699","C":"700"},"question-232-954931865":{"A":"704","B":"705","C":"706"},"question-234-920067257":{"A":"710","B":"711","C":"712"},"question-236-372325360":{"A":"716","B":"717","C":"718"},"question-183-1627814699":{"A":"550","B":"551","C":"552"},"question-185-1603473258":{"A":"556","B":"557","C":"558"},"question-187-899353468":{"A":"563","B":"564","C":"565","D":"566"},"question-189-922450327":{"A":"571","B":"572","C":"573","D":"574"},"question-493-1655080129":{"A":"1520","B":"1521","C":"1522"},"question-495-136398333":{"A":"1526","B":"1527","C":"1528"},"question-497-997832497":{"A":"1532","B":"1533","C":"1534"},"question-499-1552757802":{"A":"1538","B":"1539","C":"1540"},"question-501-626464877":{"A":"1544","B":"1545","C":"1546"}}
	 */
	public void setdata(List<JsonObject> documents, String reqproto, JsonObject quesorder, String[] results, JsonObject opt,int Questionscount) {
		data = new Data();
		
		//Iterating all Documents From Server
		for (int i = 0; i < documents.size(); i++) {
			JsonObject document = documents.get(i);
			String 	document_collection = document.get("collection").getAsString(),
					document_subtestid = (document.get("subtestId") != null)?document.get("subtestId").getAsString(): "",
					document_id = document.get("_id").getAsString(),
					document_name = document.get("name").getAsString();
			
			boolean isdocument_datetime = ( document.get("prototype") != null 
						&& document.get("prototype").getAsString().equals("datetime") )?true: false;
			
			
//			System.out.println("looking for prototype as "+reqproto+ " Collection as question and SubtestId as "+subtestId);
			if(reqproto.equals("survey") && document_collection.equals("question") && document_subtestid.equals(subtestId)){
//				System.out.println("found for prototype as "+reqproto+ " Collection as question and SubtestId as "+subtestId);
				int QNu = -1;
				String 	cqid,
						mqid = document_id;
				
				for (int j =0;j < Questionscount; j++) {
//					System.out.println("SETDATA:::::: iterating for from 0 to 40 "+j);
					if(quesorder.get(""+j) != null){
						
						cqid = quesorder.get(""+j).getAsString();
//						System.out.println("SETDATA:::::: cqid is not null "+cqid+" >> "+mqid);
						if(cqid.equals(mqid)){
							System.out.println("SETDATA:::::: Sequence Matched at Index  "+j+"");
							QNu = j;
							break;
						}
					}else if(j != 0){
						System.out.println("SETDATA:::::: Breaking if J!=0 "+j);
						break;
					}else{
						logger.log(Level.SEVERE, "Question Sequence Is Wrong");
						break;
					}
				}
				System.out.println("Qnu is "+QNu);
				if(QNu != -1){
					if(results[QNu].equals("skip")){
//						System.out.println("Skipping Question as in result");
						setsum(sum.getcorrect(), sum.getincorrect(), sum.getmissing()+1, sum.gettotal()+1);
						data.addQuestion( document_name, "0");
					}else{
						setsum(sum.getcorrect()+1, sum.getincorrect(), sum.getmissing(), sum.gettotal()+1);
						data.addQuestion( document_name,opt.get(mqid).getAsJsonObject()
										.get(results[QNu]).getAsString() );
					}
					
				}else{
					logger.log(Level.SEVERE, "skipping question forcefully");
					setsum(sum.getcorrect(), sum.getincorrect(), sum.getmissing()+1, sum.gettotal()+1);
					data.addQuestion( document_name, "0");
				}
				break;
			}else if (reqproto.equals("datetime") && isdocument_datetime) {
				data.addtime();
				break;
			}
			/*System.out.println(i+" Wasted Iteration for Collection "+document_collection
				+" Subtest id"+document_subtestid
				+" id "+document_id
				+" name "+document_name);*/
//			System.out.println(i+" Wasted Iteration for "+document.toString());
			
		}
		
	}
}
