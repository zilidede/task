package com.zl.task.craw.base;

import com.ll.drissonPage.page.ChromiumTab;
import com.ll.drissonPage.units.listener.DataPacket;

import java.io.IOException;
import java.util.List;

public interface CrawServiceXHRTab extends CrawServiceTab {
    String getXHRDir();

    void setXHRDir(String xhrDir);

    List<String> listenXHR(ChromiumTab tab) throws IOException;

    void saveXHR(ChromiumTab tab);

    void saveFile(String fileDir, DataPacket data) throws Exception;

    String getXHR();

    void setXHR(String xhr);

    void openUrl(String url, Double timeout);
}
