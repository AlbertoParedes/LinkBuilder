verificarDisponibilidad (id_cliente_param(int),  id_empleado_param(int),  fecha_param(varchart))
BEGIN
    
    DECLARE edt INT;
    DECLARE nameUserEditando VARCHAR(200);
    DECLARE idUserEditando INT;
    DECLARE follows_creados INT;
    DECLARE follows_user INT;
    DECLARE nofollows_creados INT;
    DECLARE nofollows_user INT;
    
    SELECT c.Editando, u.name, u.id INTO edt, nameUserEditando ,idUserEditando
    	FROM `lnk_clientes` c 
        JOIN `lnk_users` u ON c.userEditando = u.id
        WHERE c.id_cliente = id_cliente_param;
    
    IF edt = 0 OR idUserEditando = id_empleado_param THEN 
    	
        UPDATE `lnk_clientes` SET `Editando` = '0' WHERE `lnk_clientes`.`userEditando` = id_empleado_param;
        UPDATE `lnk_clientes` SET `Editando` = '1' WHERE `lnk_clientes`.`id_cliente` = id_cliente_param;
        UPDATE `lnk_clientes` SET `userEditando` = id_empleado_param WHERE `lnk_clientes`.`id_cliente` = id_cliente_param;
        
    	SELECT COUNT(*) into follows_creados FROM `lnk_resultado` r WHERE r.id_cliente = id_cliente_param and r.fecha LIKE CONCAT('%',fecha_param,'%') and r.tipo = 'FOLLOW';
        SELECT c.follows into follows_user FROM `lnk_clientes` c WHERE c.id_cliente = id_cliente_param; 
        
        WHILE follows_creados < follows_user DO
            INSERT INTO `lnk_resultado` VALUES (NULL,id_cliente_param, '0', '', NOW(), 'FOLLOW', '', '0', '  ', '', '-1', '0', '0', '0');
        	SET follows_creados = follows_creados + 1;
        END WHILE;
        
        SELECT COUNT(*) into nofollows_creados FROM `lnk_resultado` r WHERE r.id_cliente = id_cliente_param and r.fecha LIKE CONCAT('%',fecha_param,'%') and r.tipo = 'NOFOLLOW';
        SELECT c.nofollows into nofollows_user FROM `lnk_clientes` c WHERE c.id_cliente = id_cliente_param; 
        
        WHILE nofollows_creados < nofollows_user DO
            INSERT INTO `lnk_resultado` VALUES (NULL,id_cliente_param, '0', '', NOW(), 'NOFOLLOW', '', '0', '  ', '', '-1', '0', '0', '0');
        	SET nofollows_creados = nofollows_creados + 1;
        END WHILE;
        
        
    	SELECT '1' as 'Disponibilidad', r.*, (SELECT GROUP_CONCAT(DISTINCT url_a_atacar SEPARATOR ',') FROM `lnk_Cliente_Urls` AS cl WHERE cl.id_cliente = r.id_cliente) AS 'urls_a_atacar', ctg.`enlace` as nombreCategoria, f.web_foro, f.descripcion as 'DescripcionForo'
    		FROM `lnk_resultado` r 
    		JOIN `lnk_categorias` ctg ON r.categoria = ctg.id_categoria
    		JOIN `lnk_foros` f ON r.id_foro = f.id_foro
    		WHERE r.id_cliente = id_cliente_param AND r.fecha LIKE CONCAT('%',fecha_param,'%');
            
            SELECT f.`id_foro`, f.`web_foro`, f.`categoria`, f.`descripcion` from `lnk_foros` as f WHERE NOT EXISTS (SELECT * FROM `lnk_resultado` where `id_cliente` = id_cliente_param and f.`id_foro` = id_foro);
			
            SELECT c.* FROM `lnk_clientes` c WHERE c.id_cliente = id_cliente_param;
    
    
    
    ELSEIF edt = 1 THEN
    
   		SELECT '0' as 'Disponibilidad', nameUserEditando as 'UserEditando';
    
    END IF;
 
END