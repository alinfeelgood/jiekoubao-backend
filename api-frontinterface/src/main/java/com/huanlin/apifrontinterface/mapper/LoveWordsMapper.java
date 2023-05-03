package com.huanlin.apifrontinterface.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huanlin.apifrontinterface.model.entity.LoveWords;
//import com.yupi.apiclientsdk.model.LoveWords;

/**
 * @Entity generator.domain.LoveWords
 */
public interface LoveWordsMapper extends BaseMapper<LoveWords> {
    String getRandomLoveWords();
}




