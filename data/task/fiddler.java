if (oSession.fullUrl.Contains("weather.cma.cn/api/now")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "S:\\data\\task\\爬虫\\weather\\weather.cma.cnApiNow\\"+ts+".txt",  2 );
        } 	
