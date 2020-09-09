package testMelissari.util;

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
import org.apache.openjpa.enhance.FieldConsumer;
import org.apache.openjpa.enhance.FieldSupplier;
import org.apache.openjpa.enhance.PCEnhancer;
import org.apache.openjpa.enhance.PCRegistry;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.enhance.StateManager;


public class DummyPersistenceCapeable implements PersistenceCapable {
    private static String pcFieldNames[] = {"IntegerField"};
    private static Class pcFieldTypes[] = {Integer.class};
    private static byte pcFieldFlags[] = {};
    private static Class pcPCSuperclass;
    protected transient boolean pcVersionInit;
    protected   StateManager pcStateManager;
    static {
        Class aclass[] = new Class[0];
        pcFieldTypes = aclass;
        PCRegistry.register(DummyPersistenceCapeable.class, pcFieldNames, pcFieldTypes, pcFieldFlags, pcPCSuperclass,
                "DummyPersistenceCapeable", new DummyPersistenceCapeable());
        
    }

    @Override
    public int pcGetEnhancementContractVersion() {
        return PCEnhancer.ENHANCER_VERSION - 1;
    }

    @Override
    public PersistenceCapable pcNewInstance(StateManager sm, boolean clear) {
        return new DummyPersistenceCapeable();
    }

    @Override
    public void pcCopyFields(Object fromObject, int[] fields) {

    }

    @Override
    public void pcCopyKeyFieldsFromObjectId(FieldConsumer consumer, Object obj) {
    }

    @Override
    public void pcCopyKeyFieldsToObjectId(FieldSupplier supplier, Object obj) {
    }

    @Override
    public void pcCopyKeyFieldsToObjectId(Object obj) {
    }

    @Override
    public void pcDirty(String fieldName) {
    	if(pcStateManager!=null) {
    		pcStateManager.dirty(fieldName);
    	}
    }

    @Override
    public Object pcFetchObjectId() {
        return null;
    }

    @Override
    public Object pcGetDetachedState() {
        return null;
    }

    @Override
    public Object pcGetGenericContext() {
        return null;
    }

    @Override
    public StateManager pcGetStateManager() {
        return pcStateManager;
    }

    @Override
    public Object pcGetVersion() {
        return null;
    }

    @Override
    public boolean pcIsDeleted() {
        return false;
    }

    @Override
    public Boolean pcIsDetached() {
        return null;
    }

    @Override
    public boolean pcIsDirty() {
        return false;
    }

    @Override
    public boolean pcIsNew() {
        return false;
    }

    @Override
    public boolean pcIsPersistent() {
        return false;
    }

    @Override
    public boolean pcIsTransactional() {
        return false;
    }

    @Override
    public PersistenceCapable pcNewInstance(StateManager sm, Object obj, boolean clear) {
        return null;
    }

    @Override
    public Object pcNewObjectIdInstance() {
        return null;
    }

    @Override
    public Object pcNewObjectIdInstance(Object obj) {
        return null;
    }

    @Override
    public void pcProvideField(int fieldIndex) {

    }

    @Override
    public void pcProvideFields(int[] fieldIndices) {

    }

    @Override
    public void pcReplaceField(int fieldIndex) {

    }

    @Override
    public void pcReplaceFields(int[] fieldIndex) {

    }

    @Override
    public void pcReplaceStateManager(StateManager sm) {

    }

    @Override
    public void pcSetDetachedState(Object state) {

    }

    public DummyPersistenceCapeable() {

    }
}
