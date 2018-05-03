package com.KyLee.model;

import org.springframework.stereotype.Component;

import java.util.Hashtable;

/**
 * @program: zhihu0.1
 * @description: 字典树 结点结构
 * @author: KyLee
 * @create: 2018-05-03 20:34
 **/
public class TrieNode {

    private Hashtable<Character,TrieNode> subMap= new Hashtable<Character,TrieNode>();

    private boolean isEnd =false;

    public void SetSubNode(Character c,TrieNode trieNode){
        subMap.put(c,trieNode);
    }
    public void setEnd(boolean isEnd){
        this.isEnd=isEnd;
    }
    public boolean isEnd(){
        return isEnd;
    }
    public TrieNode getSubNode(Character c){
        return subMap.get(c);
    }

}
