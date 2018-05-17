SELECT
	pattern.photo AS pattern,
	compare.photo AS compare,
	h.visited AS priority

FROM hotel AS h
JOIN hotel_photo AS pattern ON pattern.hotel = hotel.id
JOIN hotel_photo AS compare ON compare.hotel = hotel.id AND pattern.photo <> compare.photo

/* Hledame pouze fotky, u kterych neni spocitana podbnost; zalezi na poradi! */
LEFT JOIN similarity AS s ON (s.pattern = pattern.photo AND s.compare = compare.photo) 
	OR (s.pattern = compare.photo AND s.compare = pattern.photo)

WHERE s IS NULL
ORDER BY h.visited DESC NULL LAST
LIMIT 10000;