USE umami;

-- Vérifier si la table "test" existe
IF NOT EXISTS (
    SELECT 1
    FROM information_schema.tables
    WHERE table_schema = 'umami'
    AND table_name = 'test'
) THEN
    -- Créer la table "test" si elle n'existe pas
CREATE TABLE test (
    test VARCHAR(20)
);
END IF;
