package org.openmrs.module.inventory.util;



import java.util.Comparator;

import org.openmrs.ConceptAnswer;
import org.openmrs.module.hospitalcore.model.InventoryDrugFormulation;

public class InventoryDrugFormulationComparator implements Comparator<InventoryDrugFormulation>
{

	public int compare(InventoryDrugFormulation o1, InventoryDrugFormulation o2) {
		return o1.getName().compareToIgnoreCase(o2.getName());
	}

	
}

