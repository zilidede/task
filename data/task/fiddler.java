if (oSession.fullUrl.Contains("compass.jinritemai.com/compass_api/shop/live/live_rank/board_list_v2")) {
            var fso;
            var file;
            var now = new Date();
            var month=now.getMonth()+1;
            var ts = now.getFullYear()+"-"+month+"-"+now.getDate()+"-"+now.getDay()+"-"+now.getHours()+"-"+now.getMinutes()+"-"+now.getSeconds()+"-"+now.getMilliseconds();
            fso = new ActiveXObject("Adodb.Stream");
            fso.Charset = "utf-8";
            fso.Open();
            fso.WriteText("url: " + oSession.fullUrl + " Request body: " + oSession.GetRequestBodyAsString + " Response body: " + oSession.GetResponseBodyAsString());
            fso.SaveToFile( "d:\\data\\task\\爬虫\\douYinShopApi\\compass.jinritemai.comCompassApiShopLiveLiveRankBoardListV2\\"+ts+".txt",  2 );
        } 	
