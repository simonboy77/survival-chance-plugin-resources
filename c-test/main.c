// gcc -g main.c -lm -o test
// https://oldschool.runescape.wiki/w/Ring_of_life

// https://stats.stackexchange.com/questions/208720/calculate-probability-of-random-numbers-adding-up-to-or-being-greater-than-anoth
// https://en.wikipedia.org/wiki/Irwin%E2%80%93Hall_distribution
// https://stackoverflow.com/questions/43431205/sum-of-dice-rolls
// https://stats.libretexts.org/Bookshelves/Probability_Theory/Book%3A_Introductory_Probability_(Grinstead_and_Snell)/07%3A_Sums_of_Random_Variables/7.01%3A_Sums_of_Discrete_Random_Variables

#include <stdio.h> // printf
#include <math.h>  // pow

enum HitResult
{
    Survive,
    RolTrigger,
    Death,

    ResultAmount
};

const char* string_from_result(int hitResult)
{
    switch(hitResult)
    {
        case Survive: { return "Survive"; } break;
        case RolTrigger: { return "RoL Trigger"; } break;
        case Death: { return "Death"; } break;
        default: { return ""; } break;
    }

    return "";
}

void hit_func_plus(int hitNum, int hitAmount, int hitRange, int *hitResults, int curHit, int rolThreshold, int deathThreshold)
{
    if(hitNum < hitAmount)
    {
        for(int hit = 0; hit < hitRange; ++hit)
        {
            int newHit = curHit + hit;
            
            if(newHit >= deathThreshold) {
                hitResults[Death]++;
            }
            else if(newHit >= rolThreshold) {
                hitResults[RolTrigger]++;
            }
            else {
                hit_func_plus(hitNum + 1, hitAmount, hitRange, hitResults, newHit, rolThreshold, deathThreshold);
            }
        }
    }
    else if(hitAmount)
    {
        if(curHit >= deathThreshold) {
            hitResults[Death]++;
        }
        else if(curHit >= rolThreshold) {
            hitResults[RolTrigger]++;
        }
        else {
            hitResults[Survive]++;
        }
    }
}

void brute_force_plus(int minHit, int maxHit, int hitAmount, int maxHp, int curHp)
{
    int hitRange = (maxHit - minHit) + 1;
    int hitResults[ResultAmount];

    int rolThreshold = floor(maxHp / 10.0f);
    int deathThreshold = curHp;

    hit_func_plus(0, hitAmount, hitRange, hitResults, 0, rolThreshold, deathThreshold);

    int totalHits = hitResults[Survive] + hitResults[RolTrigger] + hitResults[Death];
    printf("totalHits: %i\n", totalHits);

    for(int hitResult; hitResult < ResultAmount; ++hitResult)
    {
        float chance = (float)hitResults[hitResult] / (float)totalHits;
        printf("Frequency of %s: %f\n", string_from_result(hitResult), chance);
    }
}

void main()
{
    int maxHp = 99;
    int curHp = 46;
    int enemyMaxHit = 15;

    brute_force_plus(0, 20, 2, 99, 25);

    //brute_force(0, 20, 3);
    //hit_calc(0, 2, 2);

    return;
}