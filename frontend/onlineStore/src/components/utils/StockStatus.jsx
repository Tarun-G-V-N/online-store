import React from 'react'

const StockStatus = ({inventory}) => {
  return (
    <p>{inventory > 0 ? 
        <span className='text-success'> {inventory} In Stock</span>
        : <span className='text-danger'>Out of Stock</span>}
    </p>
                
  )
}

export default StockStatus