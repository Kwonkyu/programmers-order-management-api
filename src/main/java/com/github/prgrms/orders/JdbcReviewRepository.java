package com.github.prgrms.orders;

import static com.github.prgrms.utils.DateTimeUtils.*;

import com.github.prgrms.utils.DateTimeUtils;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcReviewRepository implements ReviewRepository {

	private final JdbcTemplate jdbcTemplate;

	public JdbcReviewRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Review save(Review review) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(
				"INSERT INTO reviews(user_seq, product_seq, content, create_at) VALUES (?, ?, ?, ?)");
			statement.setLong(1, review.getUserId());
			statement.setLong(2, review.getProductId());
			statement.setString(3, review.getContent());
			statement.setTimestamp(4, DateTimeUtils.timestampOf(review.getCreatedAt()));
			return statement;
		}, keyHolder);
		Number key = keyHolder.getKey();
		if (key == null) {
			throw new IllegalStateException("database can't insert new data now.");
		}
		review.setSeq(key.longValue());
		return review;
	}

	@Override
	public Optional<Review> findById(long id) {
		List<Review> reviews = jdbcTemplate.query(
			"SELECT * FROM reviews WHERE seq=?",
			rowMapper,
			id
		);
		return Optional.ofNullable(reviews.isEmpty() ? null : reviews.get(0));
	}

	static RowMapper<Review> rowMapper = (rs, rowNum) ->
		new Review.Builder()
		.seq(rs.getLong("seq"))
		.userId(rs.getLong("user_seq"))
		.productId(rs.getLong("product_seq"))
		.content(rs.getString("content"))
		.createdAt(dateTimeOf(rs.getTimestamp("created_at")))
		.build();
}
