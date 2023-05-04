package net.xdclass.xdclassredis.service.impl;

import net.xdclass.xdclassredis.dao.VideoCardDao;
import net.xdclass.xdclassredis.model.VideoCardDO;
import net.xdclass.xdclassredis.service.VideoCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoCardServiceImpl implements VideoCardService {

    @Autowired
    private VideoCardDao videoCardDao;

    @Override
    public List<VideoCardDO> list() {

        return videoCardDao.list();
    }
}
