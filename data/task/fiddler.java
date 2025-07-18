if (oSession.fullUrl.Contains("audience_prefer_keyword?aadvid")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "S:\\data\\task\\爬虫\\yunTu\\audiencePreferKeywordAadvid\\"+ts+".txt",  2 );
        } 	
if (oSession.fullUrl.Contains("yuntu_st/api/search_strategy/get_search_word_related")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "S:\\data\\task\\爬虫\\yunTu\\yuntuStApiSearchStrategyGetSearchWordRelated\\"+ts+".txt",  2 );
        } 	
if (oSession.fullUrl.Contains("lite_keywords_packet/get_search_word_detail")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "S:\\data\\task\\爬虫\\yunTu\\liteKeywordsPacketGetSearchWordDetail\\"+ts+".txt",  2 );
        } 	
if (oSession.fullUrl.Contains("product_matrix?aadvid")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "S:\\data\\task\\爬虫\\yunTu\\productMatrixAadvid\\"+ts+".txt",  2 );
        } 	
if (oSession.fullUrl.Contains("search_strategy/get_hot_search_word?")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "S:\\data\\task\\爬虫\\yunTu\\searchStrategyGetHotSearchWord\\"+ts+".txt",  2 );
        } 	
if (oSession.fullUrl.Contains("searchList")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "S:\\data\\task\\爬虫\\yunTu\\searchList\\"+ts+".txt",  2 );
        } 	
