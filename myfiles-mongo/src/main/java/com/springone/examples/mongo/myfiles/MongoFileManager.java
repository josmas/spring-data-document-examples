package com.springone.examples.mongo.myfiles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.datastore.document.mongodb.MongoConverter;
import org.springframework.datastore.document.mongodb.MongoTemplate;
import org.springframework.datastore.document.mongodb.SimpleMongoConverter;
import org.springframework.datastore.document.mongodb.query.QueryBuilder;
import org.springframework.stereotype.Repository;

import com.mongodb.DB;

@Repository
public class MongoFileManager {

	MongoTemplate mongoDbTemplate;
	
	@Autowired
	public void setDb(DB db) {
		MongoConverter converter = new SimpleMongoConverter();
		mongoDbTemplate = new MongoTemplate(db, converter);
	}

    public void addFiles(List<FileEntry> files) throws DataAccessException {
		for (FileEntry file : files) {
			this.mongoDbTemplate.save("myFiles", file);
		}    	
    }
    
    public List<FileEntry> queryForAllFiles() throws DataAccessException {
		List<FileEntry> results = this.mongoDbTemplate.queryForCollection("myFiles", FileEntry.class);
		return results;
    }
    
    public List<FileEntry> queryForLargeFiles(long size) throws DataAccessException {
    	QueryBuilder qb = new QueryBuilder();
    	qb.find("size").gt(size);
		List<FileEntry> results = this.mongoDbTemplate.
				queryForList("myFiles", qb.build(), FileEntry.class);
//				queryForList("myFiles", "{ 'size' : { '$gt' : " + size + " } }", FileEntry.class);
		return results;
    }
    
}