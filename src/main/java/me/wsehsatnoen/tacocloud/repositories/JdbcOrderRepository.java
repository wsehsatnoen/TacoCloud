package me.wsehsatnoen.tacocloud.repositories;

import me.wsehsatnoen.tacocloud.tacos.Taco;
import me.wsehsatnoen.tacocloud.tacos.TacoOrder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class JdbcOrderRepository implements OrderRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public TacoOrder save(TacoOrder order) {
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "INSERT INTO taco_order"
                        + "(delivery_name, delivery_street, delivery_city,"
                        + "delivery_state, delivery_zip, cc_number,"
                        + "cc_expiration, cc_cvv, placed_at)"
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, )",
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
        );
        pscf.setReturnGeneratedKeys(true);

        order.setPlacedAt(new Date());
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        order.getDeliveryName(),
                        order.getDeliveryStreet(),
                        order.getDeliveryCity(),
                        order.getDeliveryState(),
                        order.getDeliveryZip(),
                        order.getCcNumber()
                        order.getCcExpiration()
                        order.getCcCVV(),
                        order.getPlacedAt()
                )
        );

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);
        long orderId = keyHolder.getKey().longValue();
        order.setId(orderId);

        List<Taco> tacos = order.getTacos();
        int i = 0;
        for (Taco taco : tacos) {
            saveTaco(orderId, i++, taco);
        }

    }

}
