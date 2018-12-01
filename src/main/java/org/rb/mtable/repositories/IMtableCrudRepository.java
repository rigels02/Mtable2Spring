
package org.rb.mtable.repositories;

import java.util.List;
import org.rb.mtable.model.MTable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created  on 27-Feb-17.
 * @author raitis
 */
public interface IMtableCrudRepository extends CrudRepository<MTable,Long>{
    List<MTable> findByName(@Param("name") String name);
}
