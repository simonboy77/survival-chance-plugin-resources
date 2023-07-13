
private void attack_func_slow(int attackNum, int attackAmount, int curDamage, int maxDamage,
                      boolean phoenix, int escapeDamage, int deathDamage)
{
    ++this.iterationsTest;

    if(attackNum < attackAmount)
    {
        for(int damage = 0; damage <= maxDamage; ++damage)
        {
            int nextDamage = curDamage + damage;

            if(nextDamage >= deathDamage) {
                this.resultOccurrences[HitResult.RESULT_DEATH]++;
            }
            else if(nextDamage >= escapeDamage) {
                this.resultOccurrences[HitResult.RESULT_ESCAPE]++;
            }
            else {
                attack_func_slow(attackNum + 1, attackAmount, nextDamage, maxDamage, phoenix, escapeDamage, deathDamage);
            }
        }
    }
    else if(attackAmount > 0)
    {
        if(curDamage >= deathDamage) {
            this.resultOccurrences[HitResult.RESULT_DEATH]++;
        }
        else if(curDamage >= escapeDamage) {
            this.resultOccurrences[HitResult.RESULT_ESCAPE]++;
        }
        else {
            this.resultOccurrences[HitResult.RESULT_SURVIVE]++;
        }
    }
}

private void attack_func_slow(int attackAmount, int maxDamage,
                           boolean phoenix, int escapeDamage, int deathDamage)
{
    this.attack_func_slow(0, attackAmount, 0, maxDamage, phoenix, escapeDamage, deathDamage);
}

private void attack_func(int attackNum, int attackAmount, int curDamage, int maxDamage,
                      int triggerDamage, int deathDamage, boolean escapeItem, boolean phoenix)
{
    // TODO: Should I add minDamage? Now its assumed to always be zero
    // TODO: add redemption boolean, works like phoenix

    /* if phoenix gets triggered do:
        nextDamage -= phoenixHealAmount
        and pass false for the phoenix boolean, DONT CHANGE IT
    */

    ++this.iterationsTest;

    if(attackNum < attackAmount)
    {
        /*
        Do the damage for loop from high to low

        When the maxTotalDamage can trigger an escape or kill, keep going with the recursion
        otherwise, calculate how many hits would follow the current one and add those to results[SURVIVAL]


        */

        for(int damage = maxDamage; damage >= 0; --damage)
        {
            boolean triggerItem = (escapeItem || phoenix);
            int nextDamage = curDamage + damage;

            if(nextDamage >= deathDamage) {
                this.resultOccurrences[HitResult.RESULT_DEATH]++;
            }
            else if(triggerItem && nextDamage >= triggerDamage) {
                // TODO: if phoenix
                this.resultOccurrences[HitResult.RESULT_ESCAPE]++;
            }
            else // Check if all of the remaining hits are safe
            {
                int remainingHits = attackAmount - (attackNum + 1);
                int maxTotalDamage = nextDamage + (maxDamage * remainingHits);

                if((escapeItem || phoenix) && (maxTotalDamage >= triggerDamage))
                {
                    // TODO: if phoenix
                    attack_func(attackNum + 1, attackAmount, nextDamage, maxDamage, triggerDamage, deathDamage, escapeItem, phoenix);
                }
                else if(maxTotalDamage >= deathDamage)
                {
                    attack_func(attackNum + 1, attackAmount, nextDamage, maxDamage, triggerDamage, deathDamage, escapeItem, phoenix);
                }
                else // All following hits are safe
                {
                    int remainingHitOccurrences = (damage + 1) * (int)Math.pow((double)(maxDamage + 1), (double)remainingHits);
                    this.resultOccurrences[HitResult.RESULT_SURVIVE] += remainingHitOccurrences;
                    break;
                }
            }
        }
    }
    else if(attackAmount > 0)
    {
        log.info("curDamage " + curDamage);

        if(curDamage >= deathDamage) {
            this.resultOccurrences[HitResult.RESULT_DEATH]++;
        }
        else if(curDamage >= triggerDamage) {
            this.resultOccurrences[HitResult.RESULT_ESCAPE]++;
        }
        else {
            this.resultOccurrences[HitResult.RESULT_SURVIVE]++;
        }
    }
}

private void attack_func(int attackAmount, int maxDamage, int triggerDamage,
                      int deathDamage, boolean escapeItem, boolean phoenix)
{
    this.attack_func(0, attackAmount, 0, maxDamage, triggerDamage, deathDamage, escapeItem, phoenix);
}



public void calcSurvivalChanceTest()
{
    //double hitChance = this.calcHitChance(this.opponents[0]);
    //log.info("hitChance: " + hitChance);

//        int hitAmount = this.config.hitTurns();
//        //int maxHit = this.monsterStats.getMaxHit(this.opponents[0].getId());
//        int maxHp = this.client.getRealSkillLevel(Skill.HITPOINTS);
//
//        // TODO: if wearing ring of life/defence cape
//        int triggerDamage = this.hitpoints - (int)Math.floor((double)maxHp / 10.0);
//        this.resultOccurrences = new int[HitResult.RESULT_AMOUNT];
//        this.resultWeights = new double[HitResult.RESULT_AMOUNT];
//        this.resultChances = new double[HitResult.RESULT_AMOUNT];
//
//        this.iterationsTest = 0;
//
//        hitAmount = 1;
//        int maxHit = 12;
//
//        HitInfo hits[] = new HitInfo[1];
//        hits[0] = new HitInfo(0, maxHit, 0.5);
//
//        //hit_func_slow(hitAmount, maxHit, false, triggerDamage, this.hitpoints);
//        //hit_func(hitAmount, maxHit, triggerDamage, this.hitpoints, true, false);
//        hit_func_weighted(hits, triggerDamage, this.hitpoints, true, false);
//
//        // Parse results
//        double totalHits = 0.0;
//        double totalWeight = 0.0;
//
//        for(int resultId = 0; resultId < HitResult.RESULT_AMOUNT; ++resultId) {
//            totalHits += this.resultOccurrences[resultId];
//            totalWeight += this.resultWeights[resultId];
//        }
//
//        for(int resultId = 0; resultId < HitResult.RESULT_AMOUNT; ++resultId) {
//            //double chance = ((double)this.resultOccurrences[resultId] / totalHits) * 100.0;
//            double chance = (this.resultWeights[resultId] / totalWeight) * 100.0;
//
//            this.resultChances[resultId] = chance;
//        }
//
//        log.info("safety: " + this.resultChances[HitResult.RESULT_SURVIVE] +
//                ", escape: " + this.resultChances[HitResult.RESULT_ESCAPE] +
//                ", die: " + this.resultChances[HitResult.RESULT_DEATH] +
//                ", totalHits: " + totalHits +
//                ", iterations: " + this.iterationsTest);
}
