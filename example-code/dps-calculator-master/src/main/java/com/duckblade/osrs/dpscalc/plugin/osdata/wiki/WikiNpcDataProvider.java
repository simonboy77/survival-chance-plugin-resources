package com.duckblade.osrs.dpscalc.plugin.osdata.wiki;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class WikiNpcDataProvider implements NpcDataProvider
{

	private static final String NPC_STATS_URL = "npcs.min.json";
	private static final String NPC_BASE_IDS_URL = "npc-base-ids.min.json";

	private Map<Integer, NpcData> npcStatsMap;
	private Map<Integer, Integer> npcBaseIdsMap;

	@Inject
	public WikiNpcDataProvider(Gson gson, WikiDataLoader wikiDataLoader) throws IOException
	{
		wikiDataLoader.getReader(NPC_BASE_IDS_URL, reader ->
			npcBaseIdsMap = gson.fromJson(reader, new TypeToken<HashMap<Integer, Integer>>()
			{
			}.getType())
		);
		wikiDataLoader.getReader(NPC_STATS_URL, reader ->
			npcStatsMap = gson.fromJson(reader, new TypeToken<HashMap<Integer, NpcData>>()
			{
			}.getType())
		);
	}

	public Set<NpcData> getAll()
	{
		return new HashSet<>(npcStatsMap.values());
	}

	public NpcData getById(int npcId)
	{
		return npcStatsMap.get(canonicalize(npcId));
	}

	public int canonicalize(int npcId)
	{
		return npcBaseIdsMap.getOrDefault(npcId, npcId);
	}

}