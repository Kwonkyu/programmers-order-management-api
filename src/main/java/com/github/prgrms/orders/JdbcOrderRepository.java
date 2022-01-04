package com.github.prgrms.orders;

import static com.github.prgrms.utils.DateTimeUtils.*;

import com.github.prgrms.utils.DateTimeUtils;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcOrderRepository implements OrderRepository {

	private final JdbcTemplate jdbcTemplate;

	public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Optional<ProductOrder> findById(Long orderId) {
		List<ProductOrder> query = jdbcTemplate.query(
			"SELECT * FROM orders WHERE seq=?",
			rowMapper,
			orderId
		);
		return query.isEmpty() ? Optional.empty() : Optional.ofNullable(query.get(0));
	}

	@Override
	public List<ProductOrder> findAll(long userId, long offset, int size) {
		return jdbcTemplate.query(
			"SELECT * FROM orders WHERE user_seq=? ORDER BY seq DESC LIMIT ? OFFSET ?",
			rowMapper,
			userId,
			size,
			offset
		);
	}

	@Override
	public ProductOrder update(ProductOrder productOrder) {
		jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(
				"UPDATE orders SET state=?, review_seq=?, request_msg=?, reject_msg=?, completed_at=?, rejected_at=? WHERE seq=?"
			);
			statement.setString(1, productOrder.getState()
											   .name());
			if(productOrder.getReview() != null) {
				statement.setLong(2, productOrder.getReview());
			} else {
				statement.setNull(2, Types.BIGINT);
			}
			statement.setString(3, productOrder.getRequestMessage());
			statement.setString(4, productOrder.getRejectMessage());
			statement.setTimestamp(5, DateTimeUtils.timestampOf(productOrder.getCompletedAt()));
			statement.setTimestamp(6, DateTimeUtils.timestampOf(productOrder.getRejectedAt()));
			statement.setLong(7, productOrder.getSeq());
			return statement;
		});
		return productOrder;
	}

	static RowMapper<ProductOrder> rowMapper = (rs, rowNum) ->
		new ProductOrder.Builder()
			.seq(rs.getObject("seq", Long.class))
			.userId(rs.getObject("user_seq", Long.class))
			.productId(rs.getObject("product_seq", Long.class))
			.review(rs.getObject("review_seq", Long.class))
			.state(OrderState.valueOf(rs.getString("state")))
			.requestMsg(rs.getString("request_msg"))
			.rejectMsg(rs.getString("reject_msg"))
			.completedAt(dateTimeOf(rs.getTimestamp("completed_at")))
			.rejectedAt(dateTimeOf(rs.getTimestamp("rejected_at")))
			.createdAt(dateTimeOf(rs.getTimestamp("create_at")))
			.build();
}
