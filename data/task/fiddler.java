if (oSession.fullUrl.Contains("overview?product_id")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "d:\\data\\task\\爬虫\\compassBrand\\overviewProductId\\"+ts+".txt",  2 );
        } 	
if (oSession.fullUrl.Contains("word_cloud?brand_id=")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "d:\\data\\task\\爬虫\\compassBrand\\wordCloudBrandId=\\"+ts+".txt",  2 );
        } 	
if (oSession.fullUrl.Contains("author/compare_index")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "d:\\data\\task\\爬虫\\compassBrand\\authorCompareIndex\\"+ts+".txt",  2 );
        } 	
if (oSession.fullUrl.Contains("competition/discover/account")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "d:\\data\\task\\爬虫\\compassBrand\\competitionDiscoverAccount\\"+ts+".txt",  2 );
        } 	
if (oSession.fullUrl.Contains("short_video_list")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "d:\\data\\task\\爬虫\\compassBrand\\shortVideoList\\"+ts+".txt",  2 );
        } 	
if (oSession.fullUrl.Contains("strategy_crowd/crowd_custom_select/user_count")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "d:\\data\\task\\爬虫\\compassBrand\\strategyCrowdCrowdCustomSelectUserCount\\"+ts+".txt",  2 );
        } 	
if (oSession.fullUrl.Contains("strategy_crowd/crowd_list")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "d:\\data\\task\\爬虫\\compassBrand\\strategyCrowdCrowdList\\"+ts+".txt",  2 );
        } 	
if (oSession.fullUrl.Contains("auth_list?parent_id")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "d:\\data\\task\\爬虫\\compassBrand\\authListParentId\\"+ts+".txt",  2 );
        } 	
if (oSession.fullUrl.Contains("infra_service/cate_list")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "d:\\data\\task\\爬虫\\compassBrand\\infraServiceCateList\\"+ts+".txt",  2 );
        } 	
if (oSession.fullUrl.Contains("short_video_list?shop_id")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "d:\\data\\task\\爬虫\\compassBrand\\shortVideoListShopId\\"+ts+".txt",  2 );
        } 	
if (oSession.fullUrl.Contains("flow_channel?product_id")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "d:\\data\\task\\爬虫\\compassBrand\\flowChannelProductId\\"+ts+".txt",  2 );
        } 	
if (oSession.fullUrl.Contains("account_analysis?date_type")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "d:\\data\\task\\爬虫\\compassBrand\\accountAnalysisDateType\\"+ts+".txt",  2 );
        } 	
if (oSession.fullUrl.Contains("portray_data")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "d:\\data\\task\\爬虫\\compassBrand\\portrayData\\"+ts+".txt",  2 );
        } 	
if (oSession.fullUrl.Contains("strategy_dill_crowd?shop_id")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "d:\\data\\task\\爬虫\\compassBrand\\strategyDillCrowdShopId\\"+ts+".txt",  2 );
        } 	
if (oSession.fullUrl.Contains("product_analysis?date_type")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "d:\\data\\task\\爬虫\\compassBrand\\productAnalysisDateType\\"+ts+".txt",  2 );
        } 	
if (oSession.fullUrl.Contains("cross_analysis")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "d:\\data\\task\\爬虫\\compassBrand\\crossAnalysis\\"+ts+".txt",  2 );
        } 	
if (oSession.fullUrl.Contains("penetration_list?shop_id")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "d:\\data\\task\\爬虫\\compassBrand\\penetrationListShopId\\"+ts+".txt",  2 );
        } 	
if (oSession.fullUrl.Contains("flow_overview?product_id")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "d:\\data\\task\\爬虫\\compassBrand\\flowOverviewProductId\\"+ts+".txt",  2 );
        } 	
if (oSession.fullUrl.Contains("list?dim_type=cate&cate_id")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "d:\\data\\task\\爬虫\\compassBrand\\listDimType=cateCateId\\"+ts+".txt",  2 );
        } 	
if (oSession.fullUrl.Contains("action/report/")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "d:\\data\\task\\爬虫\\compassBrand\\actionReport\\"+ts+".txt",  2 );
        } 	
