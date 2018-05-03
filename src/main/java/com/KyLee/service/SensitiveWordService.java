package com.KyLee.service;

import com.KyLee.model.TrieNode;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @program: zhihu0.1
 * @description: 敏感词过滤服务
 * @author: KyLee
 * @create: 2018-05-03 20:38
 **/
@Service
public class SensitiveWordService implements InitializingBean {

    private static TrieNode root = new TrieNode() ;


    @Override
    public void afterPropertiesSet() throws Exception {
        ArrayList<String> words=new ArrayList<String>();
        words.add("abb");
        words.add("误解");
        words.add("误解了");
        words.add("abc");
        for (int i=0;i<words.size();i++){
            addTrieNode(words.get(i));
        }
        System.out.println("加载完毕");
    }

    private void addTrieNode(String word){
        if(word.isEmpty()) return ;
        TrieNode p=root;
        for (int i=0;i<word.length();i++){
            //若当前结点没有此字符的子节点，则新建
            if(p.getSubNode(word.charAt(i))==null){
                TrieNode temp = new TrieNode() ;
                p.SetSubNode(word.charAt(i),temp);
            }

            //推进到此字符结点
            p=p.getSubNode(word.charAt(i));
            if(i==word.length()-1){
                //结束，并设置关键词。
                p.setEnd(true);
                break;
            }
        }
    }


    public String filter(String text){
        if(text.isEmpty()) return text;
        StringBuilder sb =new StringBuilder();
        int start = 0;
        int position = start;

        while(position<text.length()){

            TrieNode p =root;
            while(position<text.length()&&p.getSubNode(text.charAt(position))!=null){

                p = p.getSubNode(text.charAt(position));
                //发现敏感词，start跳到后一个字符
                if(p.isEnd()){

                    sb.append("***");
                    position++;
                    start=position;
                    break;
                }
                //匹配成功，但end不为0,继续寻找下一个字符结点
                else {
                    position++;

                }
            }
            //如果最后一个字符是敏感词，则无需处理
            if(start>=text.length()){
                break;
            }else {
                //以start开始的字符串不存在敏感词
                sb.append(text.charAt(start));
                //start跳到下一字符继续寻找。
                start++;
                position = start;
            }
        }
         sb.append(text.substring(start));
        return sb.toString();
    }
}
