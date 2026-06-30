package io.github.luckyqing.resposity;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.luckyqing.entity.DictEntity;
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
public class DictResposity extends ServiceImpl<DictMapper, DictEntity> {

    public List<ConfigRespVO> listByType(String configType) {
        List<DictEntity> list = list(new LambdaQueryWrapper<DictEntity>()
                .eq(DictEntity::getConfigType, configType)
                .orderByAsc(DictEntity::getSortOrder));
        return list.stream().map(this::toRespVO).collect(Collectors.toList());
    }

    public Map<String, List<ConfigRespVO>> listAllGrouped() {
        List<DictEntity> all = list(new LambdaQueryWrapper<DictEntity>()
                .orderByAsc(DictEntity::getConfigType)
                .orderByAsc(DictEntity::getSortOrder));
        return all.stream().map(this::toRespVO)
                .collect(Collectors.groupingBy(ConfigRespVO::getConfigType, LinkedHashMap::new, Collectors.toList()));
    }

    public List<ConfigRespVO> listAll() {
        List<DictEntity> all = list(new LambdaQueryWrapper<DictEntity>()
                .orderByAsc(DictEntity::getConfigType)
                .orderByAsc(DictEntity::getSortOrder));
        return all.stream().map(this::toRespVO).collect(Collectors.toList());
    }

    public void addConfig(ConfigSaveReqVO reqVO) { save(toEntity(reqVO)); }
    public void updateConfig(ConfigSaveReqVO reqVO) { updateById(toEntity(reqVO)); }

    private ConfigRespVO toRespVO(DictEntity config) {
        ConfigRespVO vo = new ConfigRespVO();
        vo.setId(config.getId());
        vo.setConfigType(config.getConfigType());
        vo.setConfigLabel(config.getConfigLabel());
        vo.setConfigValue(config.getConfigValue());
        vo.setSortOrder(config.getSortOrder());
        vo.setStatus(config.getStatus());
        return vo;
    }

    private DictEntity toEntity(ConfigSaveReqVO reqVO) {
        DictEntity config = new DictEntity();
        config.setId(reqVO.getId());
        config.setConfigType(reqVO.getConfigType());
        config.setConfigLabel(reqVO.getConfigLabel());
        config.setConfigValue(reqVO.getConfigValue());
        config.setSortOrder(reqVO.getSortOrder());
        config.setStatus(reqVO.getStatus());
        return config;
    }
}
