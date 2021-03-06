/*
 *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.wso2.carbon.es.migration.client.internal;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.es.migration.EsMigrationException;
import org.wso2.carbon.es.migration.client.MigrateFrom200to210;
import org.wso2.carbon.es.migration.util.Constants;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.service.TenantRegistryLoader;
import org.wso2.carbon.registry.extensions.services.RXTStoragePathService;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * @scr.component name="org.wso2.carbon.es.migration.client" immediate="true"
 * @scr.reference name="realm.service"
 * interface="org.wso2.carbon.user.core.service.RealmService" cardinality="1..1"
 * policy="dynamic" bind="setRealmService" unbind="unsetRealmService"
 * @scr.reference name="registry.service"
 * interface="org.wso2.carbon.registry.core.service.RegistryService" cardinality="1..1"
 * policy="dynamic" bind="setRegistryService" unbind="unsetRegistryService"
 * @scr.reference name="registry.core.dscomponent"
 * interface="org.wso2.carbon.registry.core.service.RegistryService" cardinality="1..1"
 * policy="dynamic" bind="setRegistryService" unbind="unsetRegistryService"
 * @scr.reference name="tenant.registryloader" interface="org.wso2.carbon.registry.core.service.TenantRegistryLoader" cardinality="1..1"
 * policy="dynamic" bind="setTenantRegistryLoader" unbind="unsetTenantRegistryLoader"
 * @scr.reference name="registry.rxt.component"
 * interface="org.wso2.carbon.registry.extensions.services.RXTStoragePathService" cardinality="1..1"
 * policy="dynamic" bind="setRXTStoragePathService" unbind="unsetRXTStoragePathService"
 */

public class EsMigrationServiceComponent {

    private static final Log log = LogFactory.getLog(EsMigrationServiceComponent.class);

    protected void activate(ComponentContext context) {

        String migrateVersion = null;
        migrateVersion = System.getProperty("migrateStore");
        try {
            if (migrateVersion != null) {
                switch (migrateVersion) {
                    case Constants.VERSION_210:
                        MigrateFrom200to210 migrateFrom200to210 = new MigrateFrom200to210();
                        migrateFrom200to210.cleanOldResources();
                        migrateFrom200to210.databaseMigration();
                        migrateFrom200to210.registryResourceMigration();
                        break;
                    default:
                        log.error("The given migrate version " + migrateVersion + " is not supported. Please check the " +
                                  "version and try again.");
                        break;
                }

            }
        } catch (EsMigrationException e) {
            log.error("ES Migration  exception occurred while migrating. "
                      + e.getMessage(), e);
        }
    }

    /**
     * Method to deactivate bundle.
     *
     * @param context OSGi component context.
     */
    protected void deactivate(ComponentContext context) {
        log.info("WSO2 Enterprise Store migration bundle has been deactivated");
    }

    /**
     * Method to set registry service.
     *
     * @param registryService service to get tenant data.
     */
    protected void setRegistryService(RegistryService registryService) {
        if (log.isDebugEnabled()) {
            log.debug("Setting RegistryService for WSO2 Enterprise Store migration");
        }
        ServiceHolder.setRegistryService(registryService);
    }

    /**
     * Method to unset registry service.
     *
     * @param registryService service to get registry data.
     */
    protected void unsetRegistryService(RegistryService registryService) {
        if (log.isDebugEnabled()) {
            log.debug("Unset Registry service");
        }
        ServiceHolder.setRegistryService(null);
    }

    /**
     * Method to set realm service.
     *
     * @param realmService service to get tenant data.
     */
    protected void setRealmService(RealmService realmService) {
        if (log.isDebugEnabled()) {
            log.debug("Setting RealmService for WSO2 Enterprise Store migration");
        }
        ServiceHolder.setRealmService(realmService);
    }

    /**
     * Method to unset realm service.
     *
     * @param realmService service to get tenant data.
     */
    protected void unsetRealmService(RealmService realmService) {
        if (log.isDebugEnabled()) {
            log.debug("Unset Realm service");
        }
        ServiceHolder.setRealmService(null);
    }

    /**
     * Method to set tenant registry loader
     *
     * @param tenantRegLoader tenant registry loader
     */
    protected void setTenantRegistryLoader(TenantRegistryLoader tenantRegLoader) {
        if (log.isDebugEnabled()) {
            log.debug("Setting TenantRegistryLoader for WSO2 Enterprise Store migration");
        }
        ServiceHolder.setTenantRegLoader(tenantRegLoader);
    }

    /**
     * Method to unset tenant registry loader
     *
     * @param tenantRegLoader tenant registry loader
     */
    protected void unsetTenantRegistryLoader(TenantRegistryLoader tenantRegLoader) {
        if (log.isDebugEnabled()) {
            log.debug("Unset Tenant Registry Loader");//TODO: if debug
        }
        ServiceHolder.setTenantRegLoader(null);
    }

    /**
     * Method to set RXT storage service.
     *
     * @param rxtStoragePathService service to get RXT storage path data.
     */
    protected void setRXTStoragePathService(RXTStoragePathService rxtStoragePathService) {
        if (log.isDebugEnabled()) {
            log.debug("Setting RegistryService for WSO2 Enterprise Store migration");
        }
        ServiceHolder.setRXTStoragePathService(rxtStoragePathService);
    }

    /**
     * Method to unset RXT storage service.
     *
     * @param rxtStoragePathService service to get RXT storage path data.
     */
    protected void unsetRXTStoragePathService(RXTStoragePathService rxtStoragePathService) {
        if (log.isDebugEnabled()) {
            log.debug("Unset Registry service");
        }
        ServiceHolder.setRXTStoragePathService(null);
    }
}
