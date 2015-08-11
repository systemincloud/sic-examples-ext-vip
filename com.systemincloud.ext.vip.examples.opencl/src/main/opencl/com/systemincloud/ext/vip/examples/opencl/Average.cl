__kernel void average(__global const int* a, __global const int* b, __global int* out, int n) 
{
    int i = get_global_id(0);
    if (i >= n)
        return;

    out[i] = (95 * b[i]) / 100 + (5 * a[i]) / 100;
}