package com.sideby.activity.mapper;

import com.sideby.activity.dto.ActivityQuery;
import com.sideby.activity.entity.Activity;
import com.sideby.activity.mapper.row.ActivityDetailRow;
import com.sideby.activity.mapper.row.ActivitySummaryRow;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ActivityMapper {
    @Insert("""
        INSERT INTO activity (
          creator_id,
          title,
          description,
          category,
          city,
          location_name,
          location_address,
          cover_url,
          start_at,
          end_at,
          registration_deadline,
          capacity,
          participant_count,
          `status`
        )
        VALUES (
          #{creatorId},
          #{title},
          #{description},
          #{category},
          #{city},
          #{locationName},
          #{locationAddress},
          #{coverUrl},
          #{startAt},
          #{endAt},
          #{registrationDeadline},
          #{capacity},
          #{participantCount},
          #{status}
        )
        """)
    @Options(
        useGeneratedKeys = true,
        keyProperty = "id",
        keyColumn = "id"
    )
    int insert(Activity activity);

    @Select("""
        SELECT
          a.id,
          a.creator_id,
          u.display_name AS creator_display_name,
          u.username AS creator_username,
          a.title,
          a.description,
          a.category,
          a.city,
          a.location_name,
          a.location_address,
          a.cover_url,
          a.start_at,
          a.end_at,
          a.registration_deadline,
          a.capacity,
          a.participant_count,
          a.status,
          a.created_at,
          a.updated_at
        FROM activity a
        JOIN app_user u ON u.id = a.creator_id
        WHERE a.id = #{id}
          AND a.status = 'PUBLISHED'
        """)
    ActivityDetailRow findPublishedById(@Param("id") Long id);

    @Select("""
        SELECT
          a.id,
          a.creator_id,
          u.display_name AS creator_display_name,
          u.username AS creator_username,
          a.title,
          a.description,
          a.category,
          a.city,
          a.location_name,
          a.location_address,
          a.cover_url,
          a.start_at,
          a.end_at,
          a.registration_deadline,
          a.capacity,
          a.participant_count,
          a.status,
          a.created_at,
          a.updated_at
        FROM activity a
        JOIN app_user u ON u.id = a.creator_id
        WHERE a.id = #{id}
        """)
    ActivityDetailRow findById(@Param("id") Long id);

    @Select("""
        <script>
        SELECT
          a.id,
          a.creator_id,
          u.display_name AS creator_display_name,
          a.title,
          a.category,
          a.city,
          a.location_name,
          a.start_at,
          a.end_at,
          a.registration_deadline,
          a.capacity,
          a.participant_count,
          a.status,
          a.cover_url
        FROM activity a
        JOIN app_user u
          ON u.id = a.creator_id
        WHERE a.status = 'PUBLISHED'
        <if test="query.city != null and query.city.trim() != ''">
          AND a.city = #{query.city}
        </if>
        <if test="query.category != null and query.category.trim() != ''">
          AND a.category = #{query.category}
        </if>
        <if test="query.keyword != null and query.keyword.trim() != ''">
          AND a.title LIKE CONCAT('%', #{query.keyword}, '%')
        </if>
        ORDER BY a.start_at ASC, a.id ASC
        LIMIT #{limit} OFFSET #{offset}
        </script>
        """)
    List<ActivitySummaryRow> findPublishedPage(
        @Param("query") ActivityQuery query,
        @Param("offset") int offset,
        @Param("limit") int limit
    );

    @Select("""
        <script>
        SELECT
          COUNT(*)
        FROM activity a
        WHERE a.status = 'PUBLISHED'
        <if test="query.city != null and query.city.trim() != ''">
          AND a.city = #{query.city}
        </if>
        <if test="query.category != null and query.category.trim() != ''">
          AND a.category = #{query.category}
        </if>
        <if test="query.keyword != null and query.keyword.trim() != ''">
          AND a.title LIKE CONCAT('%', #{query.keyword}, '%')
        </if>
        </script>
        """)
    long countPublished(@Param("query") ActivityQuery query);

    @Update("""
        UPDATE activity
        SET
          title = #{title},
          description = #{description},
          category = #{category},
          city = #{city},
          location_name = #{locationName},
          location_address = #{locationAddress},
          cover_url = #{coverUrl},
          start_at = #{startAt},
          end_at = #{endAt},
          registration_deadline = #{registrationDeadline},
          capacity = #{capacity}
        WHERE id = #{id}
          AND creator_id = #{creatorId}
          AND status = 'PUBLISHED'
        """)
    int updateById(Activity activity);

    @Update("""
        UPDATE activity
        SET status = 'CANCELLED'
        WHERE id = #{id}
          AND creator_id = #{creatorId}
          AND status = 'PUBLISHED'
        """)
    int cancelById(@Param("id") Long id, @Param("creatorId") Long creatorId);
}
