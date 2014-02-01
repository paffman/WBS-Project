-- ---------------------------------------
-- wp_alloc_workpackage view
-- join between wp_allocation and workpackage
-- ---------------------------------------
CREATE VIEW wp_alloc_workpackage AS
SELECT * 
FROM 
	wp_allocation a 
	INNER JOIN 
	workpackage w 
	ON (a.fid_wp = w.id);