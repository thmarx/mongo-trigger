/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.thmarx.mongo.trigger;

/*-
 * #%L
 * mongo-trigger-index
 * %%
 * Copyright (C) 2023 Marx-Software
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import lombok.RequiredArgsConstructor;
import org.bson.Document;

/**
 *
 * @author t.marx
 */
@RequiredArgsConstructor
public class Updater implements AutoCloseable {
	
	private final MongoDatabase database;
	
	private Thread watcher;
	
	public void connect () {
		ChangeStreamIterable<Document> watch = database.watch().fullDocument(FullDocument.UPDATE_LOOKUP);
		watcher = new Thread(() -> {
			try {
				watch.forEach(this::handle);
			} catch (Exception e) {
				// nichts zu tun
			}
		}, "ChangeStreamUpdated");
		
		watcher.start();
	}
	
	public void handle (ChangeStreamDocument<Document> document) {
		if (isCollectionRelevant(document)) {
			// call all triggers
		}
	}

	private boolean isCollectionRelevant(ChangeStreamDocument<Document> document) {
		return true;
	}
	
	@Override
	public void close () {
		watcher.interrupt();
	}
}
