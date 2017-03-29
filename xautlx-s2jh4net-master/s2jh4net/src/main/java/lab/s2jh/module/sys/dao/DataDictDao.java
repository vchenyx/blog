package lab.s2jh.module.sys.dao;

import java.util.List;

import javax.persistence.QueryHint;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.module.sys.entity.DataDict;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DataDictDao extends BaseDao<DataDict, Long> {

    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    @Query("from DataDict where primaryKey=:primaryKey and parent is null")
    public DataDict findByRootPrimaryKey(@Param("primaryKey") String primaryKey);

    @Query("from DataDict where parent.id=:parentId and disabled=false order by orderRank desc")
    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    public List<DataDict> findEnabledChildrenByParentId(@Param("parentId") Long parentId);

    @Query("from DataDict order by parent asc,orderRank desc")
    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
    public List<DataDict> findAllCached();
}