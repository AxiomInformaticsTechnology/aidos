package storm.skyline.model.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import storm.skyline.model.StormSkyTuple;

@Repository
public interface StormSkyTupleRepo extends JpaRepository<StormSkyTuple, String> {

    public List<StormSkyTuple> findAllByLayer(Integer layer);

}