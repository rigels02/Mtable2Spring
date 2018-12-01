
package org.rb.mtable.repositories;

import org.rb.mtable.model.MTable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * 28/04/2017
 * @author raitis
 */
@RepositoryRestResource(collectionResourceRel = "mtable",path = "/mtable")
@CrossOrigin(origins = "*")
public interface IMtableRepositoryPages extends PagingAndSortingRepository<MTable,Long>{
    
}
