package com.semonemo.spring_server.domain.asset.repository.assetsell;

import static com.semonemo.spring_server.domain.asset.model.QAssetImage.*;
import static com.semonemo.spring_server.domain.asset.model.QAssetSell.*;

import java.util.ArrayList;
import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.semonemo.spring_server.domain.asset.model.AssetImage;
import com.semonemo.spring_server.domain.asset.model.AssetSell;

public class AssetSellRepositoryImpl implements AssetSellRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public AssetSellRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public List<AssetSell> findTopN(int size) {
		return queryFactory
			.selectFrom(assetSell)
			.orderBy(assetSell.Id.desc())
			.limit(size)
			.fetch();
	}

	@Override
	public List<AssetSell> findNextN(Long cursorId, int size) {
			return queryFactory
				.selectFrom(assetSell)
				.where(assetSell.Id.lt(cursorId))
				.orderBy(assetSell.Id.desc())
				.limit(size)
				.fetch();
	}

	@Override
	public AssetSell findByAssetId(Long assetId) {
		return queryFactory
			.selectFrom(assetSell)
			.where(assetSell.assetId.eq(assetId))
			.fetchOne();
	}

	@Override
	public void updateCount(int count, Long assetSellId) {
		queryFactory
			.update(assetSell)
			.where(assetSell.Id.eq(assetSellId))
			.set(assetSell.likeCount, assetSell.likeCount.add(count))
			.execute();
	}

	@Override
	public void plusHits(Long assetSellId) {
		queryFactory
			.update(assetSell)
			.where(assetSell.Id.eq(assetSellId))
			.set(assetSell.hits, assetSell.hits.add(1))
			.execute();
	}

	@Override
	public List<AssetSell> findByCreatorTopN( Long userid, int size) {
		return queryFactory
			.selectFrom(assetSell)
			.join(assetImage)
			.on(assetSell.assetId.eq(assetImage.Id))
			.where(assetImage.creator.eq(userid))
			.orderBy(assetSell.Id.desc())
			.limit(size)
			.fetch();
	}

	@Override
	public List<AssetSell> findByCreatorIdNextN( Long userid, Long cursorId, int size) {
		return queryFactory
			.selectFrom(assetSell)
			.join(assetImage)
			.on(assetSell.assetId.eq(assetImage.Id))
			.where(assetImage.Id.lt(cursorId)
				.and(assetImage.creator.eq(userid)))
			.orderBy(assetImage.Id.desc())
			.limit(size)
			.fetch();
	}

	@Override
	public void updateAssetPurchaseCount(Long count, Long assetSellId ) {
		 queryFactory
			.update(assetSell)
			.where(assetSell.Id.eq(assetSellId))
			.set(assetSell.purchaseCount, assetSell.purchaseCount.add(count))
			.execute();
	}

}