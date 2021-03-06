/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.openjpa.jdbc.meta.strats;

import java.sql.SQLException;

import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.meta.Strategy;
import org.apache.openjpa.jdbc.sql.RowManager;
import org.apache.openjpa.kernel.OpenJPAStateManager;

/**
 * No-op strategy for easy extension.
 *
 * @author Abe White
 */
public abstract class AbstractStrategy
    implements Strategy {

    
    private static final long serialVersionUID = 1L;

    @Override
    public String getAlias() {
        return getClass().getName();
    }

    @Override
    public void map(boolean adapt) {
    }

    @Override
    public void initialize() {
    }

    @Override
    public void insert(OpenJPAStateManager sm, JDBCStore store, RowManager rm)
        throws SQLException {
    }

    @Override
    public void update(OpenJPAStateManager sm, JDBCStore store, RowManager rm)
        throws SQLException {
    }

    @Override
    public void delete(OpenJPAStateManager sm, JDBCStore store, RowManager rm)
        throws SQLException {
    }

    @Override
    public Boolean isCustomInsert(OpenJPAStateManager sm, JDBCStore store) {
        return Boolean.FALSE;
    }

    @Override
    public Boolean isCustomUpdate(OpenJPAStateManager sm, JDBCStore store) {
        return Boolean.FALSE;
    }

    @Override
    public Boolean isCustomDelete(OpenJPAStateManager sm, JDBCStore store) {
        return Boolean.FALSE;
    }

    @Override
    public void customInsert(OpenJPAStateManager sm, JDBCStore store)
        throws SQLException {
    }

    @Override
    public void customUpdate(OpenJPAStateManager sm, JDBCStore store)
        throws SQLException {
    }

    @Override
    public void customDelete(OpenJPAStateManager sm, JDBCStore store)
        throws SQLException {
    }
}
