package io.github.luckyqing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.luckyqing.entity.Dict;
import io.github.luckyqing.mapper.DictMapper;
import io.github.luckyqing.vo.config.ConfigRespVO;
import io.github.luckyqing.vo.config.ConfigSaveReqVO;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典配置服务
 */
@Service
public class DictService extends ServiceImpl<DictMapper, Dict> {

    public List<ConfigRespVO> listByType(String configType) {
        List<Dict> list = list(new LambdaQueryWrapper<Dict>()
                .eq(Dict::getConfigType, configType)
                .orderByAsc(Dict::getSortOrder));
        return list.stream().map(this::toRespVO).collect(Collectors.toList());
    }

    public Map<String, List<ConfigRespVO>> listAllGrouped() {
        List<Dict> all = list(new LambdaQueryWrapper<Dict>()
                .orderByAsc(Dict::getConfigType)
                .orderByAsc(Dict::getSortOrder));
        return all.stream().map(this::toRespVO)
                .collect(Collectors.groupingBy(ConfigRespVO::getConfigType, LinkedHashMap::new, Collectors.toList()));
    }

    public List<ConfigRespVO> listAll() {
        List<Dict> all = list(new LambdaQueryWrapper<Dict>()
                .orderByAsc(Dict::getConfigType)
                .orderByAsc(Dict::getSortOrder));
        return all.stream().map(this::toRespVO).collect(Collectors.toList());
    }

    public void addConfig(ConfigSaveReqVO reqVO) { save(toEntity(reqVO)); }
    public void updateConfig(ConfigSaveReqVO reqVO) { updateById(toEntity(reqVO)); }

    private ConfigRespVO toRespVO(Dict config) {
        ConfigRespVO vo = new ConfigRespVO();
        vo.setId(config.getId());
        vo.setConfigType(config.getConfigType());
        vo.setConfigLabel(config.getConfigLabel());
        vo.setConfigValue(config.getConfigValue());
        vo.setSortOrder(config.getSortOrder());
        vo.setStatus(config.getStatus());
        return vo;
    }

    private Dict toEntity(ConfigSaveReqVO reqVO) {
        Dict config = new Dict();
        config.setId(reqVO.getId());
        config.setConfigType(reqVO.getConfigType());
        config.setConfigLabel(reqVO.getConfigLabel());
        config.setConfigValue(reqVO.getConfigValue());
        config.setSortOrder(reqVO.getSortOrder());
        config.setStatus(reqVO.getStatus());
        return config;
    }
}
