import React from 'react'
import {BsDash, BsPlus} from 'react-icons/bs'

const QuantityUpdater = ({disabled, quantity, onIncrease, onDecrease}) => {

  return (
    <div>
        <section style={{width: "150px"}}>
            <div className='input-group'>
                <button onClick={onDecrease} disabled={disabled} className='btn btn-outline-secondary'><BsDash/></button>
                <input type="number" name='quantity' value={quantity} readOnly disabled={disabled} className='form-control text-center'/>
                <button onClick={onIncrease} disabled={disabled} className='btn btn-outline-secondary'><BsPlus/></button>
            </div>
        </section>
    </div>
  )
}

export default QuantityUpdater