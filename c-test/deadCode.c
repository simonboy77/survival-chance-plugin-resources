
void calc_chance(int numToHitAfter2Hits, int minHit, int maxHit)
{
    int coeff1 = (2 * minHit) + 2;
    int coeff2 = (2 * maxHit) - 2;
    int coeff3 = (2 * maxHit);
    int coeff4 = (2 * minHit);
    
    if((coeff1 <= numToHitAfter2Hits) && (numToHitAfter2Hits <= coeff2))
    {
        float chance = 4.0f / ((maxHit - minHit + 1) * (maxHit - minHit + 2));
        printf("1: %.2f\n", chance);
    }
    else if((coeff2 < numToHitAfter2Hits) && (numToHitAfter2Hits <= coeff3))
    {
        float chance = 2.0f / ((maxHit - minHit + 1) * (maxHit - minHit + 2));
        printf("2: %.2f\n", chance);
    }
    else if((coeff4 <= numToHitAfter2Hits) && (numToHitAfter2Hits < coeff1))
    {
        float chance = 2.0f / ((maxHit - minHit + 1) * (maxHit - minHit + 2));
        printf("3: %.2f\n", chance);
    }
    else
    {
        printf("impossible\n");
    }
}

void dice_sum_probabilities(int n, int d) {
    // Compute the polynomial [(x+x^2+...+x^d)/d]^n.
    // The coefficient of x^k in the result is the
    // probability of n dice with d sides summing to k.
    int N = (d * n) + 1;
    
    // p is the coefficients of a degree N-1 polynomial.
    double p[N];
    for (int i = 0; i < N; ++i) {
        p[i] = (i == 0);
    }

    // After k iterations of the main loop, p represents
    // the polynomial [(x+x^2+...+x^d)/d]^k
    for (int i = 0; i < n; ++i)
    {
        // S is the rolling sum of the last d coefficients.
        double S = 0;
        
        // This loop iterates backwards through the array
        // setting p[j+d] to (p[j]+p[j+1]+...+p[j+d-1])/d.
        // To get the ends right, j ranges over a slightly
        // larger range than the array, and care is taken to
        // not write out-of-bounds, and to treat out-of-bounds
        // reads as 0.
        for (int j = N - 1; j >= -d; --j)
        {
            if (j >= 0) {
                S += p[j];
            }

            if ((j + d) < N) {
                S -= p[j + d];
                p[j + d] = S / d;
            }
        }
    }
    for (int i = n; i < N; i++) {
        printf("% 4d: %.08lf\n", i, p[i]);
    }
}

void hit_calc(int minHit, int maxHit, int hitAmount) {
    // Compute the polynomial [(x+x^2+...+x^d)/d]^n.
    // The coefficient of x^k in the result is the
    // probability of n dice with d sides summing to k.
    //int N = (d * n) + 1;
    int hitRange = (maxHit - minHit) + 1;
    int hitPossibilities = ((maxHit - minHit) * hitAmount) + 1;
    printf("hitP: %i\n", hitPossibilities);

    // p is the coefficients of a degree N-1 polynomial.
    double p[hitPossibilities];
    for (int i = 0; i < hitPossibilities; ++i) {
        //p[i] = (i == 0);
        p[i] = 1.0 / hitPossibilities;
    }

    // After k iterations of the main loop, p represents
    // the polynomial [(x+x^2+...+x^d)/d]^k
    for (int i = 1; i < hitAmount; ++i) // Skip 0
    {
        // S is the rolling sum of the last d coefficients.
        //double S = (i == 0) ? 1.0 : 0.0;
        double S = 0.0;

        // This loop iterates backwards through the array
        // setting p[j+d] to (p[j]+p[j+1]+...+p[j+d-1])/d.
        // To get the ends right, j ranges over a slightly
        // larger range than the array, and care is taken to
        // not write out-of-bounds, and to treat out-of-bounds
        // reads as 0.
        for (int j = hitPossibilities - 1; j >= -hitRange; --j)
        {
            if (j >= 0) {
                S += p[j];
            }

            int offsetIndex = j + hitRange;
            if (offsetIndex < hitPossibilities) {
                S -= p[offsetIndex];
                p[offsetIndex] = S / hitRange;
            }
        }
    }

    for (int i = 0; i < hitPossibilities; ++i) {
        printf("% 4d: %.02lf%%\n", i, p[i] * 100.0);
    }
}


void hit_func(int hitNum, int hitAmount, int hitRange, double *chanceOfHitting, double hitWeight, int curHit)
{   
    if(hitNum < hitAmount)
    {
        for(int hit = 0; hit < hitRange; ++hit)
        {
            hit_func(hitNum + 1, hitAmount, hitRange, chanceOfHitting, hitWeight, curHit + hit);
        }
    }
    else
    {
        //printf("curHit: %i\n", curHit);
        chanceOfHitting[curHit] += hitWeight;
    }
}


void brute_force(int minHit, int maxHit, int hitAmount)
{
    int hitRange = (maxHit - minHit) + 1;
    int hitPossibilities = ((maxHit - minHit) * hitAmount) + 1;
    double chanceOfHitting[hitPossibilities];

    int hitCombinations = pow(hitRange, hitAmount);
    double weightPerHit = 1.0 / hitCombinations;

    hit_func(0, hitAmount, hitRange, chanceOfHitting, weightPerHit, 0);

    /*
    for(int hitOne = 0; hitOne < hitRange; ++hitOne)
    {
        for(int hitTwo = 0; hitTwo < hitRange; ++hitTwo)
        {
            for(int hitThree = 0; hitThree < hitRange; ++hitThree)
            {
                int totalHit = hitOne + hitTwo + hitThree;
                chanceOfHitting[totalHit] += weightPerHit;
            }
        }
    }*/

    for(int i = 0; i < hitPossibilities; ++i)
    {
        printf("Chance of hitting %i = %.2f%%\n", (i + minHit), chanceOfHitting[i] * 100.0);
    }
}