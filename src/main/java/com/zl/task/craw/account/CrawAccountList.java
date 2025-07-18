package com.zl.task.craw.account;

import com.zl.task.craw.base.x.CrawSingleAccount;
import com.zl.task.vo.task.AccountVO;

import java.util.ArrayList;
import java.util.List;

//爬取账号列表
public class CrawAccountList {
    public static void main(String[] args) throws Exception {
        CrawAccountList crawler = new CrawAccountList();
        List<AccountVO> vos = crawler.getAccountList();
        for (AccountVO vo : vos) {
            crawler.craw(vo);
        }

    }

    public List<AccountVO> getAccountList() throws Exception {
        List<AccountVO> accountVOS = new ArrayList<>();
        return accountVOS;
    }

    public int craw(AccountVO vo) throws Exception {
        CrawSingleAccount.craw(vo);
        return 0;
    }

}
