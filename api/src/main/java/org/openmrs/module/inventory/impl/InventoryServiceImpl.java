/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.inventory.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.openmrs.Role;
import org.openmrs.api.APIException;
import org.openmrs.api.db.DAOException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.hospitalcore.model.InventoryDrug;
import org.openmrs.module.hospitalcore.model.InventoryDrugCategory;
import org.openmrs.module.hospitalcore.model.InventoryDrugFormulation;
import org.openmrs.module.hospitalcore.model.InventoryDrugUnit;
import org.openmrs.module.hospitalcore.model.InventoryStore;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugIndent;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugPatient;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugPatientDetail;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugTransaction;
import org.openmrs.module.hospitalcore.model.InventoryStoreDrugTransactionDetail;
import org.openmrs.module.hospitalcore.model.OpdDrugOrder;
import org.openmrs.module.hospitalcore.model.PatientSearch;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.db.InventoryDAO;
import org.openmrs.module.inventory.model.InventoryItem;
import org.openmrs.module.inventory.model.InventoryItemCategory;
import org.openmrs.module.inventory.model.InventoryItemSpecification;
import org.openmrs.module.inventory.model.InventoryItemSubCategory;
import org.openmrs.module.inventory.model.InventoryItemUnit;
import org.openmrs.module.inventory.model.InventoryStoreDrug;
import org.openmrs.module.inventory.model.InventoryStoreDrugAccount;
import org.openmrs.module.inventory.model.InventoryStoreDrugAccountDetail;
import org.openmrs.module.inventory.model.InventoryStoreDrugIndentDetail;
import org.openmrs.module.inventory.model.InventoryStoreItem;
import org.openmrs.module.inventory.model.InventoryStoreItemAccount;
import org.openmrs.module.inventory.model.InventoryStoreItemAccountDetail;
import org.openmrs.module.inventory.model.InventoryStoreItemIndent;
import org.openmrs.module.inventory.model.InventoryStoreItemIndentDetail;
import org.openmrs.module.inventory.model.InventoryStoreItemTransaction;
import org.openmrs.module.inventory.model.InventoryStoreItemTransactionDetail;

/**
 *
 */
public class InventoryServiceImpl extends BaseOpenmrsService implements InventoryService {
	
	public InventoryServiceImpl() {
	}
	
	protected InventoryDAO dao;
	
	public void setDao(InventoryDAO dao) {
		this.dao = dao;
	}
	
	/**
	 * STORE
	 */
	public List<InventoryStore> listInventoryStore(int min, int max) throws APIException {
		return dao.listInventoryStore(min, max);
	}
	
	public InventoryStore saveStore(InventoryStore store) throws APIException {
		return dao.saveStore(store);
	}
	
	public int countListStore() throws APIException {
		return dao.countListStore();
	}
	
	public InventoryStore getStoreById(Integer id) throws APIException {
		return dao.getStoreById(id);
	}
	
	public InventoryStore getStoreByRole(String role) throws APIException {
		return dao.getStoreByRole(role);
	}
	
	public InventoryStore getStoreByCollectionRole(List<Role> roles) throws APIException {
		return dao.getStoreByCollectionRole(roles);
	}
	
	public List<InventoryStore> listMainStore() throws APIException {
		return dao.listMainStore();
	}
	
	public void deleteStore(InventoryStore store) throws APIException {
		dao.deleteStore(store);
	}
	
	public List<InventoryStore> listAllInventoryStore() throws APIException {
		return dao.listAllInventoryStore();
	}
	
	public InventoryStore getStoreByName(String name) throws APIException {
		return dao.getStoreByName(name);
	}
	
	public List<InventoryStore> listStoreByMainStore(Integer mainStoreid, boolean bothMainStore) throws APIException {
		return dao.listStoreByMainStore(mainStoreid, bothMainStore);
	}
	
	/**
	 * ItemCategory
	 */
	
	public List<InventoryItemCategory> listItemCategory(String name, int min, int max) throws APIException {
		return dao.listItemCategory(name, min, max);
	}
	
	public List<InventoryItemCategory> findItemCategory(String name) throws APIException {
		return dao.findItemCategory(name);
	}
	
	public InventoryItemCategory saveItemCategory(InventoryItemCategory category) throws APIException {
		return dao.saveItemCategory(category);
	}
	
	public int countListItemCategory(String name) throws APIException {
		return dao.countListItemCategory(name);
	}
	
	public InventoryItemCategory getItemCategoryById(Integer id) throws APIException {
		return dao.getItemCategoryById(id);
	}
	
	public InventoryItemCategory getItemCategoryByName(String name) throws APIException {
		return dao.getItemCategoryByName(name);
	}
	
	public void deleteItemCategory(InventoryItemCategory category) throws APIException {
		dao.deleteItemCategory(category);
	}
	
	/**
	 * ItemSubCategory
	 */
	
	public List<InventoryItemSubCategory> listItemSubCategory(String name, int min, int max) throws APIException {
		return dao.listItemSubCategory(name, min, max);
	}
	
	public List<InventoryItemSubCategory> findItemSubCategory(String name) throws APIException {
		return dao.findItemSubCategory(name);
	}
	
	public InventoryItemSubCategory saveItemSubCategory(InventoryItemSubCategory subCategory) throws APIException {
		return dao.saveItemSubCategory(subCategory);
	}
	
	public int countListItemSubCategory(String name) throws APIException {
		return dao.countListItemSubCategory(name);
	}
	
	public InventoryItemSubCategory getItemSubCategoryById(Integer id) throws APIException {
		return dao.getItemSubCategoryById(id);
	}
	
	public InventoryItemSubCategory getItemSubCategoryByName(Integer categoryId, String name) throws APIException {
		return dao.getItemSubCategoryByName(categoryId, name);
	}
	
	public void deleteItemSubCategory(InventoryItemSubCategory subCategory) throws APIException {
		dao.deleteItemSubCategory(subCategory);
	}
	
	public List<InventoryItemSubCategory> listSubCatByCat(Integer categoryId) throws APIException {
		return dao.listSubCatByCat(categoryId);
	}
	
	/**
	 * ItemSpecification
	 */
	
	public List<InventoryItemSpecification> listItemSpecification(String name, int min, int max) throws APIException {
		return dao.listItemSpecification(name, min, max);
	}
	
	public List<InventoryItemSpecification> findItemSpecification(String name) throws APIException {
		return dao.findItemSpecification(name);
	}
	
	public InventoryItemSpecification saveItemSpecification(InventoryItemSpecification specification) throws APIException {
		return dao.saveItemSpecification(specification);
	}
	
	public int countListItemSpecification(String name) throws APIException {
		return dao.countListItemSpecification(name);
	}
	
	public InventoryItemSpecification getItemSpecificationById(Integer id) throws APIException {
		return dao.getItemSpecificationById(id);
	}
	
	public InventoryItemSpecification getItemSpecificationByName(String name) throws APIException {
		return dao.getItemSpecificationByName(name);
	}
	
	public void deleteItemSpecification(InventoryItemSpecification specification) throws APIException {
		dao.deleteItemSpecification(specification);
	}
	
	/**
	 * ItemUnit
	 */
	
	public List<InventoryItemUnit> listItemUnit(String name, int min, int max) throws APIException {
		return dao.listItemUnit(name, min, max);
	}
	
	public List<InventoryItemUnit> findItemUnit(String name) throws APIException {
		return dao.findItemUnit(name);
	}
	
	public InventoryItemUnit saveItemUnit(InventoryItemUnit unit) throws APIException {
		return dao.saveItemUnit(unit);
	}
	
	public int countListItemUnit(String name) throws APIException {
		return dao.countListItemUnit(name);
	}
	
	public InventoryItemUnit getItemUnitById(Integer id) throws APIException {
		return dao.getItemUnitById(id);
	}
	
	public InventoryItemUnit getItemUnitByName(String name) throws APIException {
		return dao.getItemUnitByName(name);
	}
	
	public void deleteItemUnit(InventoryItemUnit unit) throws APIException {
		dao.deleteItemUnit(unit);
	}
	
	/**
	 * Item
	 */
	
	public List<InventoryItem> listItem(Integer categoryId, String name, int min, int max) throws APIException {
		return dao.listItem(categoryId, name, min, max);
	}
	
	public List<InventoryItem> findItem(String name) throws APIException {
		return dao.findItem(name);
	}
	
	public InventoryItem saveItem(InventoryItem item) throws APIException {
		return dao.saveItem(item);
	}
	
	public int countListItem(Integer categoryId, String name) throws APIException {
		return dao.countListItem(categoryId, name);
	}
	
	public InventoryItem getItemById(Integer id) throws APIException {
		return dao.getItemById(id);
	}
	
	public InventoryItem getItemByName(String name) throws APIException {
		return dao.getItemByName(name);
	}
	
	public void deleteItem(InventoryItem item) throws APIException {
		dao.deleteItem(item);
	}
	
	public List<InventoryItem> findItem(Integer categoryId, String name) throws APIException {
		return dao.findItem(categoryId, name);
	}
	
	public int countItem(Integer categoryId, Integer unitId, Integer subCategoryId, Integer specificationId)
	                                                                                                        throws DAOException {
		return dao.countItem(categoryId, unitId, subCategoryId, specificationId);
	}
	
	/**
	 * Drug
	 */
	
	public List<InventoryDrug> listDrug(Integer categoryId, String name, int min, int max) throws APIException {
		return dao.listDrug(categoryId, name, min, max);
	}
	
	public List<InventoryDrug> findDrug(Integer categoryId, String name) throws APIException {
		return dao.findDrug(categoryId, name);
	}
	
	public InventoryDrug saveDrug(InventoryDrug drug) throws APIException {
		return dao.saveDrug(drug);
	}
	
	public int countListDrug(Integer categoryId, String name) throws APIException {
		return dao.countListDrug(categoryId, name);
	}
	
	public InventoryDrug getDrugById(Integer id) throws APIException {
		return dao.getDrugById(id);
	}
	
	public InventoryDrug getDrugByName(String name) throws APIException {
		return dao.getDrugByName(name);
	}
	
	public void deleteDrug(InventoryDrug drug) throws APIException {
		dao.deleteDrug(drug);
	}
	
	public List<InventoryDrug> getAllDrug() throws APIException {
		return dao.getAllDrug();
	}
	
	@Override
	public int countListDrug(Integer categoryId, Integer unitId, Integer formulationId) throws APIException {
		// TODO Auto-generated method stub
		return dao.countListDrug(categoryId, unitId, formulationId);
	}
	
	/**
	 * DrugCategory
	 */
	
	public List<InventoryDrugCategory> listDrugCategory(String name, int min, int max) throws APIException {
		return dao.listDrugCategory(name, min, max);
	}
	
	public List<InventoryDrugCategory> findDrugCategory(String name) throws APIException {
		return dao.findDrugCategory(name);
	}
	
	public InventoryDrugCategory saveDrugCategory(InventoryDrugCategory drugCategory) throws APIException {
		return dao.saveDrugCategory(drugCategory);
	}
	
	public int countListDrugCategory(String name) throws APIException {
		return dao.countListDrugCategory(name);
	}
	
	public InventoryDrugCategory getDrugCategoryById(Integer id) throws APIException {
		return dao.getDrugCategoryById(id);
	}
	
	public InventoryDrugCategory getDrugCategoryByName(String name) throws APIException {
		return dao.getDrugCategoryByName(name);
	}
	
	public void deleteDrugCategory(InventoryDrugCategory drugCategory) throws APIException {
		dao.deleteDrugCategory(drugCategory);
	}
	
	/**
	 * DrugFormulation
	 */
	
	public List<InventoryDrugFormulation> listDrugFormulation(String name, int min, int max) throws APIException {
		return dao.listDrugFormulation(name, min, max);
	}
	
	public List<InventoryDrugFormulation> findDrugFormulation(String name) throws APIException {
		return dao.findDrugFormulation(name);
	}
	
	public InventoryDrugFormulation saveDrugFormulation(InventoryDrugFormulation drugFormulation) throws APIException {
		return dao.saveDrugFormulation(drugFormulation);
	}
	
	public int countListDrugFormulation(String name) throws APIException {
		return dao.countListDrugFormulation(name);
	}
	
	public InventoryDrugFormulation getDrugFormulationById(Integer id) throws APIException {
		return dao.getDrugFormulationById(id);
	}
	
	public InventoryDrugFormulation getDrugFormulationByName(String name) throws APIException {
		return dao.getDrugFormulationByName(name);
	}
	
	public InventoryDrugFormulation getDrugFormulation(String name, String dozage) throws APIException {
		return dao.getDrugFormulation(name, dozage);
	}
	
	public void deleteDrugFormulation(InventoryDrugFormulation drugFormulation) throws APIException {
		dao.deleteDrugFormulation(drugFormulation);
	}
	
	/**
	 * DrugUnit
	 */
	
	public List<InventoryDrugUnit> listDrugUnit(String name, int min, int max) throws APIException {
		return dao.listDrugUnit(name, min, max);
	}
	
	public List<InventoryDrugUnit> findDrugUnit(String name) throws APIException {
		return dao.findDrugUnit(name);
	}
	
	public InventoryDrugUnit saveDrugUnit(InventoryDrugUnit drugUnit) throws APIException {
		return dao.saveDrugUnit(drugUnit);
	}
	
	public int countListDrugUnit(String name) throws APIException {
		return dao.countListDrugUnit(name);
	}
	
	public InventoryDrugUnit getDrugUnitById(Integer id) throws APIException {
		return dao.getDrugUnitById(id);
	}
	
	public InventoryDrugUnit getDrugUnitByName(String name) throws APIException {
		return dao.getDrugUnitByName(name);
	}
	
	public void deleteDrugUnit(InventoryDrugUnit drugUnit) throws APIException {
		dao.deleteDrugUnit(drugUnit);
	}
	
	/**
	 * StoreDrug
	 */
	
	public List<InventoryStoreDrug> listStoreDrug(Integer storeId, Integer categoryId, String drugName, Integer reOrderQty,
	                                              int min, int max) throws APIException {
		return dao.listStoreDrug(storeId, categoryId, drugName, reOrderQty, min, max);
	}
	
	public int countStoreDrug(Integer storeId, Integer categoryId, String drugName, Integer reOrderQty) throws APIException {
		return dao.countStoreDrug(storeId, categoryId, drugName, reOrderQty);
	}
	
	public InventoryStoreDrug getStoreDrugById(Integer id) throws APIException {
		return dao.getStoreDrugById(id);
	}
	
	public InventoryStoreDrug getStoreDrug(Integer storeId, Integer drugId, Integer formulationId) throws APIException {
		return dao.getStoreDrug(storeId, drugId, formulationId);
	}
	
	public InventoryStoreDrug saveStoreDrug(InventoryStoreDrug storeDrug) throws APIException {
		return dao.saveStoreDrug(storeDrug);
	}
	
	/**
	 * StoreDrugTransaction
	 */
	
	public List<InventoryStoreDrugTransaction> listStoreDrugTransaction(Integer transactionType, Integer storeId,
	                                                                    String description, String fromDate, String toDate,
	                                                                    int min, int max) throws APIException {
		return dao.listStoreDrugTransaction(transactionType, storeId, description, fromDate, toDate, min, max);
	}
	
	public InventoryStoreDrugTransaction saveStoreDrugTransaction(InventoryStoreDrugTransaction storeTransaction)
	                                                                                                             throws APIException {
		return dao.saveStoreDrugTransaction(storeTransaction);
	}
	
	public int countStoreDrugTransaction(Integer transactionType, Integer storeId, String description, String fromDate,
	                                     String toDate) throws APIException {
		return dao.countStoreDrugTransaction(transactionType, storeId, description, fromDate, toDate);
	}
	
	public InventoryStoreDrugTransaction getStoreDrugTransactionById(Integer id) throws APIException {
		return dao.getStoreDrugTransactionById(id);
	}
	
	@Override
	public InventoryStoreDrugTransaction getStoreDrugTransactionByParentId(Integer parentId) throws APIException {
		return dao.getStoreDrugTransactionByParentId(parentId);
	}
	
	/**
	 * StoreDrugTransactionDetail
	 */
	
	public List<InventoryStoreDrugTransactionDetail> listStoreDrugTransactionDetail(Integer storeId, Integer categoryId,
	                                                                                String drugName, String formulationName,
	                                                                                String fromDate, String toDate, int min,
	                                                                                int max) throws APIException {
		return dao
		        .listStoreDrugTransactionDetail(storeId, categoryId, drugName, formulationName, fromDate, toDate, min, max);
	}
	
	public InventoryStoreDrugTransactionDetail saveStoreDrugTransactionDetail(InventoryStoreDrugTransactionDetail storeTransactionDetail)
	                                                                                                                                     throws APIException {
		return dao.saveStoreDrugTransactionDetail(storeTransactionDetail);
	}
	
	public void saveOrUpdateStoreDrugTransactionDetail(InventoryStoreDrugTransactionDetail storeTransactionDetail) throws APIException {
     
		dao.saveOrUpdateStoreDrugTransactionDetail(storeTransactionDetail);
    }
	
	public int countStoreDrugTransactionDetail(Integer storeId, Integer categoryId, String drugName, String formulationName,
	                                           String fromDate, String toDate) throws APIException {
		return dao.countStoreDrugTransactionDetail(storeId, categoryId, drugName, formulationName, fromDate, toDate);
	}
	
	public InventoryStoreDrugTransactionDetail getStoreDrugTransactionDetailById(Integer id) throws APIException {
		return dao.getStoreDrugTransactionDetailById(id);
	}
	
	public List<InventoryStoreDrugTransactionDetail> getStoreDrugTransactionDetailByIdAndFormulation(Integer inventoryDrugId,Integer formulationId,Integer storeId) throws APIException {
		return dao.getStoreDrugTransactionDetailByIdAndFormulation(inventoryDrugId,formulationId,storeId);
	}
	
	public List<InventoryStoreDrugTransactionDetail> listStoreDrugTransactionDetail(Integer storeId, Integer drugId,
	                                                                                Integer formulationId,
	                                                                                boolean haveQuantity)
	                                                                                                     throws APIException {
		return dao.listStoreDrugTransactionDetail(storeId, drugId, formulationId, haveQuantity);
	}
	
	@Override
	public Integer sumCurrentQuantityDrugOfStore(Integer storeId, Integer drugId, Integer formulationId) throws APIException {
		return dao.sumCurrentQuantityDrugOfStore(storeId, drugId, formulationId);
	}
	
	@Override
	public List<InventoryStoreDrugTransactionDetail> listStoreDrugAvaiable(Integer storeId, Collection<Integer> drugs,
	                                                                       Collection<Integer> formulations)
	                                                                                                        throws APIException {
		// TODO Auto-generated method stub
		return dao.listStoreDrugAvaiable(storeId, drugs, formulations);
	}
	
	@Override
	public List<InventoryStoreDrugTransactionDetail> listTransactionDetail(Integer transactionId) throws APIException {
		// TODO Auto-generated method stub
		return dao.listTransactionDetail(transactionId);
	}
	
	@Override
	public Integer countViewStockBalance(Integer storeId, Integer categoryId, String drugName, String fromDate,
	                                     String toDate, boolean isExpiry) throws APIException {
		// TODO Auto-generated method stub
		return dao.countViewStockBalance(storeId, categoryId, drugName, fromDate, toDate, isExpiry);
	}
	
	@Override
	public List<InventoryStoreDrugTransactionDetail> listViewStockBalance(Integer storeId, Integer categoryId,
	                                                                      String drugName, String fromDate, String toDate,
	                                                                      boolean isExpiry, int min, int max)
	                                                                                                         throws APIException {
		// TODO Auto-generated method stub
		return dao.listViewStockBalance(storeId, categoryId, drugName, fromDate, toDate, isExpiry, min, max);
	}
	
	public InventoryStoreDrugTransactionDetail viewStockBalance(Integer inventoryDrugId,Integer formulationId,Integer storeId) throws APIException{
		return dao.viewStockBalance(inventoryDrugId,formulationId,storeId);
	}
	
	public InventoryStoreDrugTransactionDetail viewStockBalanceExpiry(Integer inventoryDrugId,Integer formulationId,Integer storeId) throws APIException{
		return dao.viewStockBalanceExpiry(inventoryDrugId,formulationId,storeId);
	}
	
	@Override
	public List<InventoryStoreDrugTransactionDetail> listStoreDrugTransactionDetail(Integer storeId, Integer drugId,
	                                                                                Integer formulationId, Integer isExpriry)
	                                                                                                                         throws APIException {
		// TODO Auto-generated method stub
		return dao.listStoreDrugTransactionDetail(storeId, drugId, formulationId, isExpriry);
	}
	
	@Override
	public int checkExistDrugTransactionDetail(Integer drugId) throws APIException {
		// TODO Auto-generated method stub
		return dao.checkExistDrugTransactionDetail(drugId);
	}
	
	/**
	 * InventoryStoreDrugIndent
	 */
	
	public List<InventoryStoreDrugIndent> listSubStoreIndent(Integer storeId, String name, Integer status, String fromDate,
	                                                         String toDate, int min, int max) throws APIException {
		return dao.listSubStoreIndent(storeId, name, status, fromDate, toDate, min, max);
	}
	
	@Override
	public List<InventoryStoreDrugIndent> listStoreDrugIndent(Integer StoreId, String name, String fromDate, String toDate,
	                                                          int min, int max) throws APIException {
		// TODO Auto-generated method stub
		return dao.listStoreDrugIndent(StoreId, name, fromDate, toDate, min, max);
	}
	
	@Override
	public int countStoreDrugIndent(Integer StoreId, String name, String fromDate, String toDate) throws APIException {
		// TODO Auto-generated method stub
		return dao.countStoreDrugIndent(StoreId, name, fromDate, toDate);
	}
	
	public int countSubStoreIndent(Integer storeId, String name, Integer status, String fromDate, String toDate)
	                                                                                                            throws APIException {
		return dao.countSubStoreIndent(storeId, name, status, fromDate, toDate);
	}
	
	public List<InventoryStoreDrugIndent> listMainStoreIndent(Integer id, Integer mainStoreId, Integer subStoreId,
	                                                          String name, Integer status, String fromDate, String toDate,
	                                                          int min, int max) throws APIException {
		return dao.listMainStoreIndent(id, mainStoreId, subStoreId, name, status, fromDate, toDate, min, max);
	}
	
	public int countMainStoreIndent(Integer id, Integer mainStoreId, Integer subStoreId, String name, Integer status,
	                                String fromDate, String toDate) throws APIException {
		return dao.countMainStoreIndent(id, mainStoreId, subStoreId, name, status, fromDate, toDate);
	}
	
	public InventoryStoreDrugIndent saveStoreDrugIndent(InventoryStoreDrugIndent storeDrugIndent) throws APIException {
		return dao.saveStoreDrugIndent(storeDrugIndent);
	}
	
	public InventoryStoreDrugIndent getStoreDrugIndentById(Integer id) throws APIException {
		return dao.getStoreDrugIndentById(id);
	}
	
	/**
	 * InventoryStoreDrugIndentDetail
	 */
	
	public List<InventoryStoreDrugIndentDetail> listStoreDrugIndentDetail(Integer storeId, Integer categoryId,
	                                                                      String indentName, String drugName,
	                                                                      String fromDate, String toDate, int min, int max)
	                                                                                                                       throws APIException {
		return dao.listStoreDrugIndentDetail(storeId, categoryId, indentName, drugName, fromDate, toDate, min, max);
	}
	
	public int countStoreDrugIndentDetail(Integer storeId, Integer categoryId, String indentName, String drugName,
	                                      String fromDate, String toDate) throws APIException {
		return dao.countStoreDrugIndentDetail(storeId, categoryId, indentName, drugName, fromDate, toDate);
	}
	
	public InventoryStoreDrugIndentDetail saveStoreDrugIndentDetail(InventoryStoreDrugIndentDetail storeDrugIndentDetail)
	                                                                                                                     throws APIException {
		return dao.saveStoreDrugIndentDetail(storeDrugIndentDetail);
	}
	
	public InventoryStoreDrugIndentDetail getStoreDrugIndentDetailById(Integer id) throws APIException {
		return dao.getStoreDrugIndentDetailById(id);
	}
	
	@Override
	public List<InventoryStoreDrugIndentDetail> listStoreDrugIndentDetail(Integer indentId) throws APIException {
		// TODO Auto-generated method stub
		return dao.listStoreDrugIndentDetail(indentId);
	}
	
	@Override
	public int checkExistDrugIndentDetail(Integer drugId) throws APIException {
		// TODO Auto-generated method stub
		return dao.checkExistDrugIndentDetail(drugId);
	}
	
	/**
	 * InventoryStoreDrugPatient
	 */
	public List<InventoryStoreDrugPatient> listStoreDrugPatient(Integer storeId, String name, String fromDate,
	                                                            String toDate, int min, int max,Integer billNo) throws APIException {
		return dao.listStoreDrugPatient(storeId, name, fromDate, toDate, min, max,billNo);
	}
	
	public int countStoreDrugPatient(Integer storeId, String name, String fromDate, String toDate) throws APIException {
		return dao.countStoreDrugPatient(storeId, name, fromDate, toDate);
	}
	
	public InventoryStoreDrugPatient saveStoreDrugPatient(InventoryStoreDrugPatient bill) throws APIException {
		return dao.saveStoreDrugPatient(bill);
	}
	
	public List<InventoryStoreDrugPatient> getStoreDrugPatientById(Integer id) throws APIException {
		return dao.getStoreDrugPatientById(id);
	}
	
	/**
	 * InventoryStoreDrugPatientDetail
	 */
	public List<InventoryStoreDrugPatientDetail> listStoreDrugPatientDetail(Integer storeDrugPatientDetailId)
	                                                                                                         throws APIException {
		return dao.listStoreDrugPatientDetail(storeDrugPatientDetailId);
	}
	
	public InventoryStoreDrugPatientDetail saveStoreDrugPatientDetail(InventoryStoreDrugPatientDetail storeDrugPatientDetail)
	                                                                                                                         throws APIException {
		return dao.saveStoreDrugPatientDetail(storeDrugPatientDetail);
	}
	
	public InventoryStoreDrugPatientDetail getStoreDrugPatientDetailById(Integer id) throws APIException {
		return dao.getStoreDrugPatientDetailById(id);
	}
	
	//change code
	/**
	 * StoreItem
	 */
	
	public List<InventoryStoreItem> listStoreItem(Integer storeId, Integer categoryId, String itemName, Integer reorderQty,
	                                              int min, int max) throws APIException {
		return dao.listStoreItem(storeId, categoryId, itemName, reorderQty, min, max);
	}
	
	public int countStoreItem(Integer storeId, Integer categoryId, String itemName, Integer reorderQty) throws APIException {
		return dao.countStoreItem(storeId, categoryId, itemName, reorderQty);
	}
	
	public InventoryStoreItem getStoreItemById(Integer id) throws APIException {
		return dao.getStoreItemById(id);
	}
	
	public InventoryStoreItem getStoreItem(Integer storeId, Integer itemId, Integer specificationId) throws APIException {
		return dao.getStoreItem(storeId, itemId, specificationId);
	}
	
	public InventoryStoreItem saveStoreItem(InventoryStoreItem StoreItem) throws APIException {
		return dao.saveStoreItem(StoreItem);
	}
	
	/**
	 * StoreItemTransaction
	 */
	
	public List<InventoryStoreItemTransaction> listStoreItemTransaction(Integer transactionType, Integer storeId,
	                                                                    String description, String fromDate, String toDate,
	                                                                    int min, int max) throws APIException {
		return dao.listStoreItemTransaction(transactionType, storeId, description, fromDate, toDate, min, max);
	}
	
	public InventoryStoreItemTransaction saveStoreItemTransaction(InventoryStoreItemTransaction storeTransaction)
	                                                                                                             throws APIException {
		return dao.saveStoreItemTransaction(storeTransaction);
	}
	
	public int countStoreItemTransaction(Integer transactionType, Integer storeId, String description, String fromDate,
	                                     String toDate) throws APIException {
		return dao.countStoreItemTransaction(transactionType, storeId, description, fromDate, toDate);
	}
	
	public InventoryStoreItemTransaction getStoreItemTransactionById(Integer id) throws APIException {
		return dao.getStoreItemTransactionById(id);
	}
	
	public InventoryStoreItemTransaction getStoreItemTransactionByParentId(Integer parentId) throws APIException {
		return dao.getStoreItemTransactionByParentId(parentId);
	}
	
	/**
	 * StoreItemTransactionDetail
	 */
	
	public List<InventoryStoreItemTransactionDetail> listStoreItemTransactionDetail(Integer storeId, Integer categoryId,
	                                                                                String itemName,
	                                                                                String specificationName,
	                                                                                String fromDate, String toDate, int min,
	                                                                                int max) throws APIException {
		return dao.listStoreItemTransactionDetail(storeId, categoryId, itemName, specificationName, fromDate, toDate, min,
		    max);
	}
	
	public InventoryStoreItemTransactionDetail saveStoreItemTransactionDetail(InventoryStoreItemTransactionDetail storeTransactionDetail)
	                                                                                                                                     throws APIException {
		return dao.saveStoreItemTransactionDetail(storeTransactionDetail);
	}
	
	public int countStoreItemTransactionDetail(Integer storeId, Integer categoryId, String itemName,
	                                           String specificationName, String fromDate, String toDate) throws APIException {
		return dao.countStoreItemTransactionDetail(storeId, categoryId, itemName, specificationName, fromDate, toDate);
	}
	
	public InventoryStoreItemTransactionDetail getStoreItemTransactionDetailById(Integer id) throws APIException {
		return dao.getStoreItemTransactionDetailById(id);
	}
	
	public List<InventoryStoreItemTransactionDetail> listStoreItemTransactionDetail(Integer storeId, Integer itemId,
	                                                                                Integer specificationId,
	                                                                                boolean haveQuantity)
	                                                                                                     throws APIException {
		return dao.listStoreItemTransactionDetail(storeId, itemId, specificationId, haveQuantity);
	}
	
	public List<InventoryStoreItemTransactionDetail> listStoreItemAvaiable(Integer storeId, Collection<Integer> items,
	                                                                       Collection<Integer> specifications)
	                                                                                                          throws APIException {
		return dao.listStoreItemAvaiable(storeId, items, specifications);
	}
	
	public Integer sumStoreItemCurrentQuantity(Integer storeId, Integer itemId, Integer specificationId) throws APIException {
		return dao.sumStoreItemCurrentQuantity(storeId, itemId, specificationId);
	}
	
	public List<InventoryStoreItemTransactionDetail> listStoreItemTransactionDetail(Integer transactionId)
	                                                                                                      throws APIException {
		return dao.listStoreItemTransactionDetail(transactionId);
	}
	
	public Integer countStoreItemViewStockBalance(Integer storeId, Integer categoryId, String itemName, String fromDate,
	                                              String toDate) throws APIException {
		return dao.countStoreItemViewStockBalance(storeId, categoryId, itemName, fromDate, toDate);
	}
	
	public List<InventoryStoreItemTransactionDetail> listStoreItemViewStockBalance(Integer storeId, Integer categoryId,
	                                                                               String itemName, String fromDate,
	                                                                               String toDate, int min, int max)
	                                                                                                               throws APIException {
		return dao.listStoreItemViewStockBalance(storeId, categoryId, itemName, fromDate, toDate, min, max);
	}
	
	public List<InventoryStoreItemTransactionDetail> listStoreItemTransactionDetail(Integer storeId, Integer itemId,
	                                                                                Integer specificationId, int min, int max)
	                                                                                                                          throws APIException {
		return dao.listStoreItemTransactionDetail(storeId, itemId, specificationId, min, max);
	}
	
	@Override
	public int checkExistItemTransactionDetail(Integer itemId) throws APIException {
		// TODO Auto-generated method stub
		return dao.checkExistItemTransactionDetail(itemId);
	}
	
	/**
	 * InventoryStoreItemIndent
	 */
	
	public List<InventoryStoreItemIndent> listStoreItemIndent(Integer StoreId, String name, String fromDate, String toDate,
	                                                          int min, int max) throws APIException {
		return dao.listStoreItemIndent(StoreId, name, fromDate, toDate, min, max);
	}
	
	public int countStoreItemIndent(Integer StoreId, String name, String fromDate, String toDate) throws APIException {
		return dao.countStoreItemIndent(StoreId, name, fromDate, toDate);
	}
	
	public List<InventoryStoreItemIndent> listSubStoreItemIndent(Integer storeId, String name, Integer status,
	                                                             String fromDate, String toDate, int min, int max)
	                                                                                                              throws APIException {
		return dao.listSubStoreItemIndent(storeId, name, status, fromDate, toDate, min, max);
	}
	
	public int countSubStoreItemIndent(Integer storeId, String name, Integer status, String fromDate, String toDate)
	                                                                                                                throws APIException {
		return dao.countSubStoreItemIndent(storeId, name, status, fromDate, toDate);
	}
	
	public List<InventoryStoreItemIndent> listMainStoreItemIndent(Integer id, Integer mainStoreId, Integer subStoreId,
	                                                              String name, Integer status, String fromDate,
	                                                              String toDate, int min, int max) throws APIException {
		return dao.listMainStoreItemIndent(id, mainStoreId, subStoreId, name, status, fromDate, toDate, min, max);
	}
	
	public int countMainStoreItemIndent(Integer id, Integer mainStoreId, Integer subStoreId, String name, Integer status,
	                                    String fromDate, String toDate) throws APIException {
		return dao.countMainStoreItemIndent(id, mainStoreId, subStoreId, name, status, fromDate, toDate);
	}
	
	public InventoryStoreItemIndent saveStoreItemIndent(InventoryStoreItemIndent storeItemIndent) throws APIException {
		return dao.saveStoreItemIndent(storeItemIndent);
	}
	
	public InventoryStoreItemIndent getStoreItemIndentById(Integer id) throws APIException {
		return dao.getStoreItemIndentById(id);
	}
	
	/**
	 * InventoryStoreItemIndentDetail
	 */
	
	public List<InventoryStoreItemIndentDetail> listStoreItemIndentDetail(Integer storeId, Integer categoryId,
	                                                                      String indentName, String itemName,
	                                                                      String fromDate, String toDate, int min, int max)
	                                                                                                                       throws APIException {
		return dao.listStoreItemIndentDetail(storeId, categoryId, indentName, itemName, fromDate, toDate, min, max);
	}
	
	public int countStoreItemIndentDetail(Integer storeId, Integer categoryId, String indentName, String itemName,
	                                      String fromDate, String toDate) throws APIException {
		return dao.countStoreItemIndentDetail(storeId, categoryId, indentName, itemName, fromDate, toDate);
	}
	
	public List<InventoryStoreItemIndentDetail> listStoreItemIndentDetail(Integer indentId) throws APIException {
		return dao.listStoreItemIndentDetail(indentId);
	}
	
	public InventoryStoreItemIndentDetail saveStoreItemIndentDetail(InventoryStoreItemIndentDetail storeItemIndentDetail)
	                                                                                                                     throws APIException {
		return dao.saveStoreItemIndentDetail(storeItemIndentDetail);
	}
	
	public InventoryStoreItemIndentDetail getStoreItemIndentDetailById(Integer id) throws APIException {
		return dao.getStoreItemIndentDetailById(id);
	}
	
	@Override
	public int checkExistItemIndentDetail(Integer itemId) throws APIException {
		// TODO Auto-generated method stub
		return dao.checkExistItemIndentDetail(itemId);
	}
	
	/**
	 * InventoryStoreItemAccount
	 */
	public List<InventoryStoreItemAccount> listStoreItemAccount(Integer storeId, String name, String fromDate,
	                                                            String toDate, int min, int max) throws APIException {
		return dao.listStoreItemAccount(storeId, name, fromDate, toDate, min, max);
	}
	
	public int countStoreItemAccount(Integer storeId, String name, String fromDate, String toDate) throws APIException {
		return dao.countStoreItemAccount(storeId, name, fromDate, toDate);
	}
	
	public InventoryStoreItemAccount saveStoreItemAccount(InventoryStoreItemAccount issue) throws APIException {
		return dao.saveStoreItemAccount(issue);
	}
	
	public InventoryStoreItemAccount getStoreItemAccountById(Integer id) throws APIException {
		return dao.getStoreItemAccountById(id);
	}
	
	/**
	 * InventoryStoreItemAccountDetail
	 */
	public List<InventoryStoreItemAccountDetail> listStoreItemAccountDetail(Integer storeItemAccountDetailId)
	                                                                                                         throws APIException {
		return dao.listStoreItemAccountDetail(storeItemAccountDetailId);
	}
	
	public InventoryStoreItemAccountDetail saveStoreItemAccountDetail(InventoryStoreItemAccountDetail storeItemAccountDetail)
	                                                                                                                         throws APIException {
		return dao.saveStoreItemAccountDetail(storeItemAccountDetail);
	}
	
	public InventoryStoreItemAccountDetail getStoreItemAccountDetailById(Integer id) throws APIException {
		return dao.getStoreItemAccountDetailById(id);
	}
	
	/**
	 * InventoryStoreDrugAccount
	 */
	public List<InventoryStoreDrugAccount> listStoreDrugAccount(Integer storeId, String name, String fromDate,
	                                                            String toDate, int min, int max) throws APIException {
		return dao.listStoreDrugAccount(storeId, name, fromDate, toDate, min, max);
	}
	
	public int countStoreDrugAccount(Integer storeId, String name, String fromDate, String toDate) throws APIException {
		return dao.countStoreDrugAccount(storeId, name, fromDate, toDate);
	}
	
	public InventoryStoreDrugAccount saveStoreDrugAccount(InventoryStoreDrugAccount issue) throws APIException {
		return dao.saveStoreDrugAccount(issue);
	}
	
	public InventoryStoreDrugAccount getStoreDrugAccountById(Integer id) throws APIException {
		return dao.getStoreDrugAccountById(id);
	}
	
	/**
	 * InventoryStoreDrugAccountDetail
	 */
	public List<InventoryStoreDrugAccountDetail> listStoreDrugAccountDetail(Integer storeDrugAccountDetailId)
	                                                                                                         throws APIException {
		return dao.listStoreDrugAccountDetail(storeDrugAccountDetailId);
	}
	
	public InventoryStoreDrugAccountDetail saveStoreDrugAccountDetail(InventoryStoreDrugAccountDetail storeDrugAccountDetail)
	                                                                                                                         throws APIException {
		return dao.saveStoreDrugAccountDetail(storeDrugAccountDetail);
	}
	
	public InventoryStoreDrugAccountDetail getStoreDrugAccountDetailById(Integer id) throws APIException {
		return dao.getStoreDrugAccountDetailById(id);
	}
	//order from opd
	public List<OpdDrugOrder> listOfDrugOrder(Integer patientId, Integer encounterId) throws APIException {
		return dao.listOfDrugOrder(patientId,encounterId);
	}
	
	public OpdDrugOrder getOpdDrugOrder(Integer patientId,Integer encounterId,Integer inventoryDrugId,Integer formulationId) throws APIException {
		return dao.getOpdDrugOrder(patientId,encounterId,inventoryDrugId,formulationId);
	}
	public List<OpdDrugOrder> listOfOrder(Integer patientId,Date date) throws APIException {
		return dao.listOfOrder(patientId,date);
	}
	public int countSearchListOfPatient(Date date, String searchKey,int page) throws APIException {
		return dao.countSearchListOfPatient(date,searchKey,page);
	}
	public List<PatientSearch> searchListOfPatient(Date date, String searchKey,int page) throws APIException {
		return dao.searchListOfPatient(date,searchKey,page);
	}
	 public List<PatientSearch> searchListOfPatient(Date date, String searchKey,int page,int pgSize) throws APIException {
			return dao.searchListOfPatient(date,searchKey,page,pgSize);
		}
		// bill id
		@Override
		public List<InventoryStoreDrugPatient> listPatientDetail() throws APIException {
			// TODO Auto-generated method stub
			return dao.listPatientDetail();
		}
		public List<InventoryStoreDrugTransaction> listTransaction() throws APIException {
			// TODO Auto-generated method stub
			return dao.listTransaction();
		}
		@Override
		public List<InventoryStoreDrugTransactionDetail> listTransactionDetailByDrugFormulation(
				Integer drugId, Integer formulationId) throws APIException {
			// TODO Auto-generated method stub
			return dao.listTransactionDetailByDrugFormulation(drugId,formulationId);
		}

}
