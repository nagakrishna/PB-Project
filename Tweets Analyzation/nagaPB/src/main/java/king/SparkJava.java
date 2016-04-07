package king;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SparkJava {

    public SparkJava() {
    }

    public enum Query {

        LOCATION,
        CRAZE,
        LANGUAGE,
        SOURCE,
        CELEBRITIES,
        DAY,
        STATS,
        TIMEZONE
    }
    public static void main(String args[])  {
////        SparkConf sparkConf = new SparkConf().setAppName("JavaSparkSQL").setMaster("local").setSparkHome("/home/lakshman/Downloads/spark-1.6.1").set("spark.driver.allowMultipleContexts", "true").set("spark.driver.allowMultipleContexts", "true");
////        JavaSparkContext ctx = new JavaSparkContext(sparkConf);
////        SQLContext sqlContext = new SQLContext(ctx);
////        String path = "/home/lakshman/Desktop/PB_NEW/0315_1.txt";
////        DataFrame df = sqlContext.read().json(path);
////        System.out.println("1");
////        df.registerTempTable("tweets");
////
////        DataFrame results = sqlContext.sql("select user.screen_name, text, coordinates, user.profile_image_url from tweets where coordinates IS NOT NULL");
////        JSONObject q  = LocationTweets(results);
////        System.out.print("nag");
//        String dfs = QueryExecution();
//        System.out.println(dfs);
    }

    public static String QueryExecution(String query)  {

        SparkConf sparkConf = new SparkConf().setAppName("JavaSparkSQL").setMaster("local").setSparkHome("/home/lakshman/Downloads/spark-1.6.1").set("spark.driver.allowMultipleContexts", "true");
        JavaSparkContext ctx = new JavaSparkContext(sparkConf);
        SQLContext sqlContext = new SQLContext(ctx);
        String path = "/home/lakshman/Desktop/PB_NEW/concat.txt";
        DataFrame df = sqlContext.read().json(path);
        df.registerTempTable("tweets");
        String output = null;
        Query s = Query.valueOf(query);
        DataFrame results;
        switch(s)
        {
            case LOCATION:
//                results = sqlContext.sql("select user.screen_name, text, coordinates, user.profile_image_url from tweets where coordinates IS NOT NULL");
                results = sqlContext.sql("select user.name, text, geo, user.profile_image_url from tweets where geo IS NOT NULL");
                output = LocationTweets(results);
                break;
            case CRAZE:
                results = sqlContext.sql("select  count(*) total, sum(case when lower(text) like '%trump%' or text like '%donald%' then 1 else 0 end) Trump, sum(case when lower(text) like '%sanders%' or lower(text) like '%bernie%'then 1 else 0 end) Sanders, sum(case when lower(text) like '%hillary%' or text like '%clinton%' then 1 else 0 end) Clinton, sum(case when lower(text) like '%ted%' or text like '%cruz%' then 1 else 0 end) Cruz, sum(case when lower(text) like '%john%' or text like '%kasich%' then 1 else 0 end) John from tweets");
                output = CrazeTweets(results);
                break;
            case LANGUAGE:
                results = sqlContext.sql("SELECT user.lang, count(*) as lang_user_count from tweets WHERE user.lang IS NOT NULL GROUP BY user.lang ORDER BY lang_user_count DESC LIMIT 10");
                output = LanguageTweets(results);
                break;
            case SOURCE:
                results = sqlContext.sql("SELECT source, count(source) as c from tweets group by source order by c desc limit 10");
                output = SourceTweets(results);
                break;
            case CELEBRITIES:
                results = sqlContext.sql("SELECT distinct user.name,user.followers_count as c, user.profile_image_url, user.description  FROM tweets where user.verified = true ORDER BY c desc limit 25");
                output = celebritiesTweets(results);
                break;
            case DAY:
                results = sqlContext.sql("select substr(user.created_at, 1, 3) as day, count(*) as count from tweets where user.created_at is not null group by substr(user.created_at, 1, 3) order by count DESC");
                output = daywiseTweets(results);
                break;
            case STATS:
                results = sqlContext.sql("select distinct user.name, user.statuses_count as count from tweets order by count desc limit 100");
                output = statsTweets(results);
                break;
            case TIMEZONE:
                results = sqlContext.sql("select user.time_zone, count(*) AS count from tweets where user.time_zone is not null GROUP BY user.time_zone ORDER BY count DESC limit 10");
                output = timezoneTweets(results);
                break;
            default :
                output = "Invalid Query";
        }
        return output;
    }

    public static String LocationTweets(DataFrame results)  {
//        RDD<String> jb = results.toJSON();

        List<String> nameList = results.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                return row.getAs(0);
            }
        }).collect();

        List<String> textList = results.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                return row.getAs(1);
            }
        }).collect();

        List<String> coordinatesList = results.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                String temp = row.getAs(2).toString();
                String a = temp.replace("[WrappedArray(","").replace("),Point]","");
//                String a = temp.replace("WrappedArray(","").replace(")","");
                return a;
            }
        }).collect();

        List<String> imageUrlList = results.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                return row.getAs(3);
            }
        }).collect();

        String[] name = new String[nameList.size()];
        nameList.toArray(name);

        String[] text = new String[textList.size()];
        textList.toArray(text);


        String[] imageUrl = new String[imageUrlList.size()];
        imageUrlList.toArray(imageUrl);

        String[] coordinates = new String[coordinatesList.size()];
        coordinatesList.toArray(coordinates);



//        String[][] Query1 = new String[4][];
//        Query1[0] = new String[name.length];
//        Query1[1] = new String[text.length];
//        Query1[2] = new String[imageUrl.length];
//        Query1[3] = new String[coordinates.length];
//        Query1[0] = name;
//        Query1[1] = text;
//        Query1[2] = imageUrl;
//        Query1[3] = coordinates;

        JSONObject obj = new JSONObject();
        JSONArray list = new JSONArray();
        try{
            for(int i=0; i<name.length; i++){
                JSONObject jo = new JSONObject();
//                text[i].replace("@", "").replace("#","").replace("/","");
                jo.put("name", name[i]);
                jo.put("text", text[i]);
                jo.put("imageUrl", imageUrl[i]);

                String[] splits = coordinates[i].split(",");

//                System.out.println("splits.size: " + splits.length);

                for(String asset: splits){
                    jo.put("lat", splits[0]);
                    jo.put("lon", splits[1]);

                }

                jo.put("coordinates", coordinates[i]);
                list.put(jo);
            }
            obj.put("result", list);
            String k = obj.toString();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return obj.toString();
    }

    public static String CrazeTweets(DataFrame results){

        List<String> totalList = results.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                return row.getAs(0).toString();
            }
        }).collect();

        List<String> trumpList = results.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                return row.getAs(1).toString();
            }
        }).collect();

        List<String> sandersList = results.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                return row.getAs(2).toString();
            }
        }).collect();

        List<String> clintonList = results.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                return row.getAs(3).toString();
            }
        }).collect();

        List<String> cruzList = results.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                return row.getAs(4).toString();
            }
        }).collect();

        List<String> johnList = results.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                return row.getAs(5).toString();
            }
        }).collect();

        String[] total = new String[totalList.size()];
        totalList.toArray(total);

        String[] trump = new String[trumpList.size()];
        trumpList.toArray(trump);

        String[] sanders = new String[sandersList.size()];
        sandersList.toArray(sanders);

        String[] clinton = new String[clintonList.size()];
        clintonList.toArray(clinton);

        String[] cruz = new String[cruzList.size()];
        cruzList.toArray(cruz);

        String[] john = new String[johnList.size()];
        johnList.toArray(john);

        JSONObject obj = new JSONObject();
        JSONArray list = new JSONArray();
        try{
            JSONObject jo = new JSONObject();
            jo.put("total", total[0]);
            jo.put("trump", trump[0]);
            jo.put("sanders", sanders[0]);
            jo.put("clinton", clinton[0]);
            jo.put("cruz", cruz[0]);
            jo.put("john", john[0]);
            list.put(jo);
            obj.put("result", list);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return obj.toString();

    }

    public static String LanguageTweets(DataFrame results)  {
        List<String> langList = results.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                return row.getAs(0).toString();
            }
        }).collect();

        List<String> countList = results.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                return row.getAs(1).toString();
            }
        }).collect();

        String[] lang = new String[langList.size()];
        String[] langTextValue = new String[langList.size()];
        langList.toArray(lang);

        //Twiiter language codoes(key) to Language name(value)
        Map<String, String> languageCodes = new HashMap<String, String>();
        languageCodes.put("en", "English");
        languageCodes.put("ar","Arabic");
        languageCodes.put("bn","Bengali");
        languageCodes.put("cs","Czech");
        languageCodes.put("da","Danish");
        languageCodes.put("de","German");
        languageCodes.put("el","Greek");
        languageCodes.put("es","Spanish");
        languageCodes.put("fa","Persian");
        languageCodes.put("fi","Finnish");
        languageCodes.put("fil","Filipino");
        languageCodes.put("fr","French");
        languageCodes.put("he","Hebrew");
        languageCodes.put("hi","Hindi");
        languageCodes.put("hu","Hungarian");
        languageCodes.put("id","Indonesian");
        languageCodes.put("it","Italian");
        languageCodes.put("ja","Japanese");
        languageCodes.put("ko","Korean");
        languageCodes.put("msa","Malay");
        languageCodes.put("nl","Dutch");
        languageCodes.put("no","Norwegian");
        languageCodes.put("pl","Polish");
        languageCodes.put("pt","Portuguese");
        languageCodes.put("ro","Romanian");
        languageCodes.put("ru","Russian");
        languageCodes.put("sv","Swedish");
        languageCodes.put("th","Thai");
        languageCodes.put("tr","Turkish");
        languageCodes.put("uk","Ukrainian");
        languageCodes.put("ur","Urdu");
        languageCodes.put("vi","Vietnamese");
        languageCodes.put("zh-cn","Chinese (Simplified)");
        languageCodes.put("zh-tw","Chinese (Traditional)");
        languageCodes.put("en-gb","English UK");



        for(int i=0; i<lang.length;i++){
            if(languageCodes.get(lang[i]) != null){
                langTextValue[i] = languageCodes.get(lang[i]);
            }
            else {
                langTextValue[i] = lang[i];
            }
//            langTextValue[i] = languageCodes.get(lang[i]);
        }

        String[] count = new String[countList.size()];
        countList.toArray(count);

//        JSONObject obj = new JSONObject();
        JSONArray children = new JSONArray();
        try{
//            obj.put("\"name\"", "temp");
           // obj.put("name", "temp");
            for(int i=0; i<lang.length; i++) {
                JSONObject obj = new JSONObject();
//                JSONObject jo = new JSONObject();
////                jo.put("\"name\"", lang[i]);
//                jo.put("name", langTextValue[i]);
//                JSONArray children2 = new JSONArray();
//                JSONObject jo2 = new JSONObject();
////                jo2.put("\"name\"", lang[i]);
//                jo2.put("name", langTextValue[i]);
////                jo2.put("\"size\"", count[i]);
//                jo2.put("size", count[i]);
//                children2.put(jo2);
////                jo.put("\"children\"", children2);
//                jo.put("children", children2);
//                children.put(jo);
////                children.put(children2);
//            }
//            obj.put("\"children\"", children);
//            obj.put("children", children);

                obj.put("lang", langTextValue[i]);
                obj.put("count", count[i]);
                children.put(obj);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return children.toString();
    }

    public static String SourceTweets(DataFrame results)  {
        List<String> sourceList = results.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                return row.getAs(0).toString();
            }
        }).collect();

        List<String> countList = results.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                return row.getAs(1).toString();
            }
        }).collect();

        String[] source = new String[sourceList.size()];
        sourceList.toArray(source);


        String[] count = new String[countList.size()];
        countList.toArray(count);

        String[] sourceNew = new String[source.length];

        for(int i=0; i<source.length; i++){
            if (source[i].matches("(?i).*iphone.*")){
                sourceNew[i] = "Iphone";
            }
            else if (source[i].matches("(?i).*android.*")){
                sourceNew[i] = "Android";
            }
            else if (source[i].matches("(?i).*Web Client.*")){
                sourceNew[i] = "Web Client";
            }
            else if (source[i].matches("(?i).*iPad.*")){
                sourceNew[i] = "iPad";
            }
            else if (source[i].matches("(?i).*twitterfeed.*")){
                sourceNew[i] = "Twitter Feed";
            }
            else if (source[i].matches("(?i).*TweetDeck.*")){
                sourceNew[i] = "TweetDeck";
            }
            else if (source[i].matches("(?i).*IFTTT.*")){
                sourceNew[i] = "IFTTT";
            }
            else if (source[i].matches("(?i).*dlvr.*")){
                sourceNew[i] = "dlvr";
            }
            else if (source[i].matches("(?i).*Facebook.*")){
                sourceNew[i] = "Facebook";
            }
            else if (source[i].matches("(?i).*Mobile Web.*")){
                sourceNew[i] = "Mobile Web";
            }
            else if (source[i].matches("(?i).*Mobile Web (M5).*")){
                sourceNew[i] = "Mobile Web (M5)";
            }
            else if (source[i].matches("(?i).*Biyon.*")){
                sourceNew[i] = "Biyon";
            }
            else if (source[i].matches("(?i).*Google.*")){
                sourceNew[i] = "Google";
            }
            else if (source[i].matches("(?i).*Biyon.*")){
                sourceNew[i] = "Biyon";
            }
            else if (source[i].matches("(?i).*Google.*")){
                sourceNew[i] = "Google";
            }
            else if (source[i].matches("(?i).*Biyon.*")){
                sourceNew[i] = "Biyon";
            }
            else if (source[i].matches("(?i).*Google.*")){
                sourceNew[i] = "Google";
            }
            else if (source[i].matches("(?i).*Biyon.*")){
                sourceNew[i] = "Biyon";
            }
            else if (source[i].matches("(?i).*Google.*")){
                sourceNew[i] = "Google";
            }
            else if (source[i].matches("(?i).*Linkis.*")){
                sourceNew[i] = "Linkis";
            }
            else if (source[i].matches("(?i).*Hootsuite.*")){
                sourceNew[i] = "Hootsuite";
            }
            else if (source[i].matches("(?i).*RoundTeam.*")){
                sourceNew[i] = "RoundTeam";
            }
            else if (source[i].matches("(?i).*Windows.*")){
                sourceNew[i] = "Windows Phone";
            }
            else if (source[i].matches("(?i).*WordPress.*")){
                sourceNew[i] = "WordPress.com";
            }
            else if (source[i].matches("(?i).*Tweetbot for iΟS.*")){
                sourceNew[i] = "Tweetbot for iΟS";
            }
            else if (source[i].matches("(?i).*TweetCaster for Android.*")){
                sourceNew[i] = "TweetCaster for Android";
            }
            else {
                sourceNew[i] = source[i];
            }
        }

        JSONObject obj = new JSONObject();
        JSONArray children = new JSONArray();
        try{
            for(int i=0; i<sourceNew.length; i++){
                JSONObject jo = new JSONObject();
                jo.put("source", sourceNew[i]);
                jo.put("count", count[i]);
                children.put(jo);
            }
            obj.put("source1", children);
            String k = obj.toString();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return obj.toString();
    }

    public static String celebritiesTweets(DataFrame results){

        List<String> nameList = results.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                return row.getAs(0).toString();
            }
        }).collect();

        List<String> countList = results.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                return row.getAs(1).toString();
            }
        }).collect();

        List<String> imageList = results.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                return row.getAs(2).toString();
            }
        }).collect();

        List<String> descriptionList = results.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                return row.getAs(3).toString();
            }
        }).collect();

        String[] name = new String[nameList.size()];
        nameList.toArray(name);

        String[] count = new String[countList.size()];
        countList.toArray(count);

        String[] image = new String[imageList.size()];
        imageList.toArray(image);

        String[] desc = new String[descriptionList.size()];
        descriptionList.toArray(desc);

        String[] nameNew = new String[nameList.size()];
        String[] countNew = new String[nameList.size()];
        String[] imageNew = new String[nameList.size()];
        String[] descNew = new String[nameList.size()];
        int c=0;
        for(int i=0; i<name.length;i++){
            boolean check = true;
            for(int j=i-1; j>=0; j--){
                if (name[i].equals(name[j])){
                    check = false;
                }

            }
            if (check==true){
                nameNew[c] = name[i];
                countNew[c] = count[i];
                imageNew[c] = image[i];
                descNew[c] = desc[i];
                c++;
            }
        }

        int lengthJson;
        if (nameNew.length > 10){
            lengthJson = 10;
        }
        else {
            lengthJson = nameNew.length;
        }

        JSONObject obj = new JSONObject();
        JSONArray children = new JSONArray();
        try{
            for(int i=0; i<lengthJson; i++){
                JSONObject jo = new JSONObject();
                jo.put("name", nameNew[i]);
                jo.put("count", countNew[i]);
                jo.put("imageUrl", imageNew[i]);
                jo.put("description", descNew[i]);
                children.put(jo);
            }
            obj.put("source1", children);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return obj.toString();

    }

    public static String daywiseTweets(DataFrame results){

        List<String> dayList = results.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                return row.getAs(0).toString();
            }
        }).collect();

        List<String> countList = results.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                return row.getAs(1).toString();
            }
        }).collect();


        String[] day = new String[dayList.size()];
        dayList.toArray(day);

        String[] count = new String[countList.size()];
        countList.toArray(count);

        String[] dayNewValue = new String[dayList.size()];

        Map<String, String> dayCodes = new HashMap<String, String>();
        dayCodes.put("Mon", "Monday");
        dayCodes.put("Tue", "Tuesday");
        dayCodes.put("Wed", "Wednesday");
        dayCodes.put("Thu", "Thursday");
        dayCodes.put("Fri", "Friday");
        dayCodes.put("Sat", "Saturday");
        dayCodes.put("Sun", "Sunday");

        for(int i=0; i<day.length;i++){
            if(dayCodes.get(day[i]) != null){
                dayNewValue[i] = dayCodes.get(day[i]);
            }
            else {
                dayNewValue[i] = day[i];
            }
        }   

        JSONObject obj = new JSONObject();
        JSONArray children = new JSONArray();
        try{
            for(int i=0; i<count.length; i++){
                JSONObject jo = new JSONObject();
                jo.put("day", dayNewValue[i]);
                jo.put("count", count[i]);
                children.put(jo);
            }
            obj.put("day",children);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return children.toString();
    }

    public static String statsTweets(DataFrame results) {

        List<String> nameList = results.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                return row.getAs(0).toString();
            }
        }).collect();

        List<String> countList = results.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                return row.getAs(1).toString();
            }
        }).collect();


        String[] name = new String[nameList.size()];
        nameList.toArray(name);

        String[] count = new String[countList.size()];
        countList.toArray(count);

        String[] countNew = new String[nameList.size()];
        String[] nameNew = new String[nameList.size()];
        int c=0;
        for(int i=0; i<name.length;i++){
            boolean check = true;
            for(int j=i-1; j>=0; j--){
                if (name[i].equals(name[j])){
                    check = false;
                }

            }
            if (check==true){
                nameNew[c] = name[i];
                countNew[c] = count[i];
                c++;
            }
        }

        int lengthJson;
        if (nameNew.length > 10){
            lengthJson = 10;
        }
        else {
            lengthJson = nameNew.length;
        }

        JSONArray children = new JSONArray();
        try{
            for(int i=0; i<lengthJson; i++){
                JSONObject jo = new JSONObject();
                jo.put("name", nameNew[i]);
                jo.put("count", countNew[i]);
                children.put(jo);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return children.toString();
    }

    public static String timezoneTweets(DataFrame results) {
        List<String> timezoneList = results.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                return row.getAs(0).toString();
            }
        }).collect();

        List<String> countList = results.javaRDD().map(new Function<Row, String>() {
            public String call(Row row) {
                return row.getAs(1).toString();
            }
        }).collect();


        String[] timezone = new String[timezoneList.size()];
        timezoneList.toArray(timezone);

        String[] count = new String[countList.size()];
        countList.toArray(count);

        String[] timezoneNewValue = new String[timezoneList.size()];

        for(int i=0; i<timezone.length; i++) {
            if (timezone[i].matches("(?i).*Eastern.*")) {
                timezoneNewValue[i] = "Eastern Time";
            } else if (timezone[i].matches("(?i).*Pacific.*")) {
                timezoneNewValue[i] = "Pacific Time";
            } else if (timezone[i].matches("(?i).*Central.*")) {
                timezoneNewValue[i] = "Central Time";
            } else if (timezone[i].matches("(?i).*Atlantic.*")) {
                timezoneNewValue[i] = "Atlantic Time";
            } else if (timezone[i].matches("(?i).*Mountain.*")) {
                timezoneNewValue[i] = "Mountain Time";
            }
            else {
                timezoneNewValue[i] = timezone[i];
            }
        }


        JSONArray children = new JSONArray();
        try{
            for(int i=0; i<timezoneNewValue.length; i++){
                JSONObject jo = new JSONObject();
                jo.put("timezone", timezoneNewValue[i]);
                jo.put("count", count[i]);
                children.put(jo);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return children.toString();

    }

}
